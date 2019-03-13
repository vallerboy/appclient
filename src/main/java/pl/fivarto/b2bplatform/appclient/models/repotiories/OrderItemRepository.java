package pl.fivarto.b2bplatform.appclient.models.repotiories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.fivarto.b2bplatform.appclient.models.entites.OrderItemEntity;
import pl.fivarto.b2bplatform.appclient.models.entites.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItemEntity, Integer> {

}
