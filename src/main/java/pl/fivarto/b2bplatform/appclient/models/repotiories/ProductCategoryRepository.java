package pl.fivarto.b2bplatform.appclient.models.repotiories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.fivarto.b2bplatform.appclient.models.entites.ProductCategoryEntity;
import pl.fivarto.b2bplatform.appclient.models.entites.ProductEntity;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends CrudRepository<ProductCategoryEntity, Integer> {
    boolean existsByCategory_IdAndProduct_Id(int categoryId, int productId);

    @Modifying
    void deleteByCategory_IdAndProduct_Id(int categoryId, int productId);

    @Modifying
    void deleteByProductId(int productId);

    List<ProductCategoryEntity> findByCategory_IdOrderByProduct_NameAsc(int categoryId);

    @Query(
            value = "select c from ProductCategoryEntity c join c.product join c.category where c.category.id = ?1",
            countQuery = "select count(c) from ProductCategoryEntity c"
    )
    Page<ProductCategoryEntity> findByCategory_Id(int categoryId, Pageable pageable);

}
