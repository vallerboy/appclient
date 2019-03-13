package pl.fivarto.b2bplatform.appclient.models.entites;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product_category")
public class ProductCategoryEntity {
    private @Id @GeneratedValue int id;
    private @OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "product_id") ProductEntity product;
    private @OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "category_id") CategoryEntity category;
}
