package pl.fivarto.b2bplatform.appclient.models.repotiories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.fivarto.b2bplatform.appclient.models.entites.CategoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {
    List<CategoryEntity> findByParentIsNullOrderByNameAsc();
    List<CategoryEntity> findByParent_IdOrderByNameAsc(int id);
    List<CategoryEntity> findAllByOrderByNameAsc();
    List<CategoryEntity> findAllByParentIsNullOrderByNameAsc();
    boolean existsByNameEqualsIgnoreCase(String name);

    Optional<CategoryEntity> findByNameEqualsIgnoreCase(String name);

}
