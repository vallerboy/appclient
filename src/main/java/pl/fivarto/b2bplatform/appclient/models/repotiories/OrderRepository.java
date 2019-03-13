package pl.fivarto.b2bplatform.appclient.models.repotiories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.fivarto.b2bplatform.appclient.models.entites.CustomerEntity;
import pl.fivarto.b2bplatform.appclient.models.entites.OrderEntity;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {
    Optional<OrderEntity> findFirstByIsClosedFalseAndCustomer(CustomerEntity cu);

}
