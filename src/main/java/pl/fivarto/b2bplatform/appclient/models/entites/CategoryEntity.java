package pl.fivarto.b2bplatform.appclient.models.entites;


import lombok.Data;
import pl.fivarto.b2bplatform.appclient.models.services.CategoryService;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "category")
public class CategoryEntity {
    private @Id @GeneratedValue int id;
    private String name;

    @OneToOne
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("name ASC")
    private List<CategoryEntity> children;

    public CategoryEntity() { }

    public CategoryEntity(int id){
        this.id = id;
    }

    public CategoryEntity(String name, Integer parent) {
        this.name = name;

        if(parent == 0) {
            this.parent = null;
        }else{
            CategoryEntity categoryEntityWithId = new CategoryEntity();
            categoryEntityWithId.setId(parent);

            this.parent = categoryEntityWithId;
        }
    }


    @Override
    public String toString() {
        return "CategoryEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", children=" + children +
                '}';
    }


}
