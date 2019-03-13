package pl.fivarto.b2bplatform.appclient.models.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.fivarto.b2bplatform.appclient.models.UserSession;
import pl.fivarto.b2bplatform.appclient.models.entites.OrderEntity;
import pl.fivarto.b2bplatform.appclient.models.entites.OrderItemEntity;
import pl.fivarto.b2bplatform.appclient.models.entites.ProductEntity;
import pl.fivarto.b2bplatform.appclient.models.repotiories.OrderItemRepository;
import pl.fivarto.b2bplatform.appclient.models.repotiories.OrderRepository;

import java.util.Comparator;
import java.util.Optional;

@Service
public class OrderService {

    final OrderRepository orderRepository;
    final UserSession userSession;
    final EmailService emailService;
    final OrderItemRepository orderItemRepository;


    @Value("${admin.email}")
    String adminEmail;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserSession userSession, EmailService emailService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userSession = userSession;
        this.emailService = emailService;
        this.orderItemRepository = orderItemRepository;
    }


    public void manageProductInOrder(int productId, int quantity) {
        OrderEntity order = getOrCreateLastOrderForUser();

        if (order.isThisProductInOrder(productId)) {
            if (quantity == 0) {
                removeItemFromOrder(productId);
                return;
            }

            changeQuantityOfItemInOrder(productId, order, quantity);
            return;
        }

        addProductToOrder(productId, quantity, order);
    }

    private void addProductToOrder(int productId, int quantity, OrderEntity order) {
        OrderItemEntity orderItemEntity = new OrderItemEntity(order,
                new ProductEntity(productId),
                quantity);

        orderItemRepository.save(orderItemEntity);

        userSession.refreshSession();
    }

    private void changeQuantityOfItemInOrder(int productId, OrderEntity order, int quantity) {
        for (OrderItemEntity orderItem : order.getOrderItems()) {
            if (orderItem.getProduct().getId() == productId) {
                orderItem.setCount(quantity);

                orderItemRepository.save(orderItem);
                break;
            }
        }

        userSession.refreshSession();
    }


    public OrderEntity getOrCreateLastOrderForUser() {
        Optional<OrderEntity> order = orderRepository.findFirstByIsClosedFalseAndCustomer(userSession.getCustomer());
        if(!order.isPresent()){
            createNewOrder();
        }

        while (!order.isPresent()) {
            order = orderRepository.findFirstByIsClosedFalseAndCustomer(userSession.getCustomer());
        }
        return order.get();
    }

    private OrderEntity createNewOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomer(userSession.getCustomer());
        return orderRepository.save(orderEntity);
    }

    private void closeLastOrder() {
        Optional<OrderEntity> order = orderRepository.findFirstByIsClosedFalseAndCustomer(userSession.getCustomer());
        order.map(s -> {
            s.setIsClosed(true);
            return s;
        }).ifPresent(orderRepository::save);
    }


    public void makeOrder(String orderInfo) {
        String itemsAndSummaryForUser = createSummaryStringFromOrderForUser(getOrCreateLastOrderForUser());
        String adminString = createSummaryStringFromOrderForAdmin(getOrCreateLastOrderForUser(), orderInfo);

        emailService.sendEmail(userSession.getCustomer().getEmail(), EmailService.EmailType.ORDER_DONE_USER, itemsAndSummaryForUser);
        emailService.sendEmail(adminEmail, EmailService.EmailType.ORDER_DONE_ADMIN, adminString);

        closeLastOrder();
    }

    private String createSummaryStringFromOrderForUser(OrderEntity orderEntity) {
        StringBuilder data = new StringBuilder("Twoje przedmioty: <br>");
        data.append(createHtmlTableFromProducts(orderEntity, false));
        return data.toString();
    }

    private String createSummaryStringFromOrderForAdmin(OrderEntity orderEntity, String orderInfo) {
        StringBuilder data = new StringBuilder("Zamówione przedmioty: <br>");

        data.append(createHtmlTableFromProducts(orderEntity, true));

        data.append("<br><br>Dane kontrahenta: ").append("<br/>");
        data.append("Klient NAZWA: ").append(orderEntity.getCustomer().getCompanyName()).append("<br>");
        data.append("Klient NIP: ").append(orderEntity.getCustomer().getNip()).append("<br>");
        data.append("Klient LOGIN: ").append(orderEntity.getCustomer().getLogin()).append("<br>");
        //data.append("Klient EMAIL: ").append(orderEntity.getCustomer().getEmail()).append("<br>");
        //data.append("Klient TELEFON: ").append(orderEntity.getCustomer().getPhone()).append("<br>");


        data.append("<br>").append("INFO OD KLIENTA: ").append(orderInfo);
        return data.toString();
    }


    private String createHtmlTableFromProducts(OrderEntity orderEntity, boolean isForAdmin) {
        sortProductsInOrderByGroup(orderEntity);


        StringBuilder tableHtml = new StringBuilder("<br/><table border=\"1\">");
        tableHtml.append("<tr>\n")
                .append("       <th>EAN</th>\n")
                .append("       <th>Nazwa</th>\n");

        if (isForAdmin)
            tableHtml.append("       <th>Grupa</th>\n");

        tableHtml.append("       <th>Sztuki</th>\n")
                .append("</tr>");

        for (OrderItemEntity orderItem : orderEntity.getOrderItems()) {
            tableHtml.append("<tr>");
            tableHtml.append("<td>").append(orderItem.getProduct().getEan()).append("</td>");
            tableHtml.append("<td>").append(orderItem.getProduct().getName()).append("</td>");
            ;

            if (isForAdmin)
                tableHtml.append("<td>").append(orderItem.getProduct().getCategoriesSexyList()).append("</td>");
            ;

            tableHtml.append("<td>").append(orderItem.getCount()).append("</td>");
            tableHtml.append("</tr>");
        }
        tableHtml.append("</table>");
        return tableHtml.toString();
    }

    private void sortProductsInOrderByGroup(OrderEntity orderEntity) {
        orderEntity.getOrderItems().sort(Comparator.comparing(s -> s.getProduct().getGroup()));
    }

    public void clearOrder() {
        Optional<OrderEntity> orderEntity = orderRepository.findFirstByIsClosedFalseAndCustomer(userSession.getCustomer());
        orderEntity.ifPresent(s -> {
            for (OrderItemEntity orderItem : s.getOrderItems()) {
                orderItemRepository.deleteById(orderItem.getId());
            }
        });
        userSession.refreshSession();
    }

    public void removeItemFromOrder(int itemId) {
        Optional<OrderEntity> orderEntity = orderRepository.findFirstByIsClosedFalseAndCustomer(userSession.getCustomer());
        if (orderEntity.isPresent()) {
            OrderEntity orderEntityNoOptional = orderEntity.get();
            findAndDeleteFromOrderProductWithId(itemId, orderEntityNoOptional);
        }

        userSession.refreshSession();
    }

    private void findAndDeleteFromOrderProductWithId(int itemId, OrderEntity orderEntityNoOptional) {
        for (OrderItemEntity orderItem : orderEntityNoOptional.getOrderItems()) {
            if (orderItem.getProduct().getId() == itemId) {
                orderItemRepository.deleteById(orderItem.getId());
                break;
            }
        }
    }


}
