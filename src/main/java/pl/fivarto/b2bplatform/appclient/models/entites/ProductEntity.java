package pl.fivarto.b2bplatform.appclient.models.entites;


import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import pl.fivarto.b2bplatform.appclient.models.dto.ProductDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "product")
public class ProductEntity {
    private @Id @GeneratedValue int id;
    private @Column(name = "external_id") int externalId;
    private String name;
    private float quantity;
    private String ean;
    private @Column(name = "is_display") boolean isDisplay;
    private @Column(name = "price_netto") float priceNetto;
    private float vat;
    private @Column(name = "last_edit") LocalDateTime lastEditTime;
    private @Column(name = "is_data_edited_by_website") boolean isDataEdited;
    private String description;

    private @NotFound(action= NotFoundAction.IGNORE) @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = {}) List<ProductCategoryEntity> categories;

    private @Column(name = "delivery_group") String group;
    private @Column(name = "delivery_group_who") String groupWho;

    public ProductEntity(ProductDto productDto){
         this.name = productDto.getName();
         this.quantity = productDto.getCount();
         this.priceNetto = productDto.getPriceNetto();
         this.vat = productDto.getVat();
         this.externalId = productDto.getExternalId();
         this.ean = productDto.getEan();
         this.group = productDto.getGroup();
         this.groupWho = productDto.getGroupWho();
         this.description = "";
         this.isDataEdited = false;
         this.isDisplay = false;
    }

    public ProductEntity(int id){
        this.id = id;
    }

    public boolean checkIfProductHasCategoryWithId(int id){
         return categories.stream().map(ProductCategoryEntity::getCategory).anyMatch(s -> s.getId() == id);
    }

    public String getCategoriesSexyList() {
        return categories.stream()
                .map(s -> s.getCategory().getName())
                .collect(Collectors.joining(", "));
    }

}
