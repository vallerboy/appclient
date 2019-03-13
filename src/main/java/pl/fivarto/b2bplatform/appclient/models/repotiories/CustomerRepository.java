package pl.fivarto.b2bplatform.appclient.models.repotiories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.fivarto.b2bplatform.appclient.models.entites.CustomerEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Integer> {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    List<CustomerEntity> findAllByOrderByRegistrationDateDesc();
    boolean existsByLoginAndPassword(String login, String password);
    Optional<CustomerEntity> findByLogin(String login);
    Optional<CustomerEntity> findByLoginAndPasswordAndIsAcceptedIsTrue(String login, String password);
}
