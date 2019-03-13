package pl.fivarto.b2bplatform.appclient.models.repotiories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.fivarto.b2bplatform.appclient.models.entites.ProductEntity;

import javax.persistence.criteria.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Integer> {
     Optional<ProductEntity> findByExternalId(int externalId);
     Page<ProductEntity> findAllByOrderByName(Pageable pageable);
     Page<ProductEntity> findAllByLastEditTimeIsAfterAndIsDataEditedIsFalse(Pageable pageable, LocalDateTime localDate);
     List<ProductEntity> findAllByLastEditTimeIsAfterAndQuantityLessThanEqual(LocalDateTime localDate, float quantity);
     Page<ProductEntity> findAllByNameContainsIgnoreCaseOrderByName(String text, Pageable pageable);
     List<ProductEntity> findAllByNameContainsIgnoreCaseOrderByName(String text);

     @Query(nativeQuery = true, value = "SELECT *  FROM product WHERE (LOWER(`name`) LIKE LOWER(CONCAT('%',?1,'%')) OR `ean` LIKE CONCAT('%',?2,'%')) AND `is_display` = '1'")
     List<ProductEntity> findAllByNameContainsIgnoreCaseOrEanContainsAndIsDisplayIsTrue(String text, String ean);
     Page<ProductEntity> findAllByIsDisplayIsTrue(Pageable pageable);
}
