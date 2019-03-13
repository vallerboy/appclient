package pl.fivarto.b2bplatform.appclient.models.entites;


import lombok.Data;
import org.hibernate.criterion.Order;
import pl.fivarto.b2bplatform.appclient.models.services.ProductService;

import javax.persistence.*;

@Data
@Table(name = "order_items")
@Entity
public class OrderItemEntity {
    private @Id @GeneratedValue  int id;
    private @OneToOne @JoinColumn(name = "order_id") OrderEntity order;
    private @OneToOne @JoinColumn(name = "product_id") ProductEntity product;
    private int count;

    public OrderItemEntity(OrderEntity order, ProductEntity product, int count) {
        this.order = order;
        this.product = product;
        this.count = count;
    }

    public OrderItemEntity() {
    }
}
