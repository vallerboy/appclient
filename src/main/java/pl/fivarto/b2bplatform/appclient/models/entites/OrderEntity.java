package pl.fivarto.b2bplatform.appclient.models.entites;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {
    private @Id @GeneratedValue int id;
    private @OneToOne @JoinColumn(name = "customer_id") CustomerEntity customer;
    private @Column(name = "creation_date") LocalDateTime creationDate;
    private @Column(name = "is_closed") boolean isClosed;
    private String info;
    private @OneToMany(mappedBy = "order", fetch = FetchType.EAGER) List<OrderItemEntity> orderItems;


    public boolean isThisProductInOrder(int productId){
        return orderItems.stream()
                .anyMatch(s -> s.getProduct().getId() == productId);
    }

    public int getOrderProductCount(int productId){
        if(orderItems == null){
            return 0;
        }
        return orderItems.stream()
                .filter(s -> s.getProduct().getId() == productId)
                .findFirst()
                .map(OrderItemEntity::getCount)
                .orElse(0);
    }


    public boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean closed) {
        isClosed = closed;
    }
}
