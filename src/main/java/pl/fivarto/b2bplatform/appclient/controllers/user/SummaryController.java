package pl.fivarto.b2bplatform.appclient.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.fivarto.b2bplatform.appclient.models.UserSession;
import pl.fivarto.b2bplatform.appclient.models.services.CategoryService;
import pl.fivarto.b2bplatform.appclient.models.services.OrderService;
import pl.fivarto.b2bplatform.appclient.models.services.ProductService;
import retrofit2.http.Path;

@Controller
public class SummaryController {

    final UserSession userSession;
    final CategoryService categoryService;
    final OrderService orderService;
    final ProductService productService;

    @Autowired
    public SummaryController(UserSession userSession, CategoryService categoryService, OrderService orderService, ProductService productService) {
        this.userSession = userSession;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @ModelAttribute
    public void addOrderToAllRequest(Model model){
        model.addAttribute("order", orderService.getOrCreateLastOrderForUser());
        model.addAttribute("allCategories", categoryService.getAllCategoriesWithoutParent());

    }

    @GetMapping("/summary")
    public String index(Model model) {
        return "user/summary";
    }


    @GetMapping("/summary/makeOrder/{info}")
    public String makeOrder(Model model,
                            @PathVariable(value = "info", required = false) String orderInfo) {

        orderService.makeOrder(orderInfo == null ? "Brak infromacji" : orderInfo);

        model.addAttribute("allCategories", categoryService.getAllCategoriesWithoutParent());
        return "user/orderMakeCorrectly";
    }

    @GetMapping("/summary/makeOrder/")
    public String makeOrder(Model model) {
        return "redirect:/summary/makeOrder/brak";
    }

    @GetMapping("/summary/clearOrder")
    public String clearOrder() {
        orderService.clearOrder();
        return "redirect:/summary";
    }

    @GetMapping("/transaction/remove/{id}")
    public String remove(@PathVariable("id") int id) {
        orderService.removeItemFromOrder(id);
        return "redirect:/summary";
    }

    @GetMapping("/transaction/product/change/{productId}/{quantity}")
    public String remove(@PathVariable("productId") int productId,
                         @PathVariable("quantity") int quantity) {
        orderService.manageProductInOrder(productId, quantity);
        return "redirect:/summary";
    }


    @PostMapping("/transaction/add/{productId}")
    public String editTransaction(@PathVariable("productId") int productId,
                                  @RequestParam("howMany") int howMany){
        orderService.manageProductInOrder(productId, howMany);
        return "redirect:/";
    }

    @GetMapping("/transaction/add/{productId}/{howMany}")
    @ResponseBody
    public String editTransactionAsync(@PathVariable("productId") int productId,
                                  @PathVariable("howMany") int howMany){
        orderService.manageProductInOrder(productId, howMany);
        return "ok";
    }


}
