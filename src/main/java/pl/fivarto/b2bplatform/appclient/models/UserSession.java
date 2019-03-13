package pl.fivarto.b2bplatform.appclient.models;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import pl.fivarto.b2bplatform.appclient.models.entites.CustomerEntity;
import pl.fivarto.b2bplatform.appclient.models.repotiories.CustomerRepository;

@Component
@Data
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {
    private CustomerEntity customer;
    private boolean isLogin;


    final CustomerRepository customerRepository;

    @Autowired
    public UserSession(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public void logout() {
        this.isLogin = false;
        this.customer = null;
    }


    public void refreshSession(){
        this.customer = customerRepository.findById(customer.getId()).orElseThrow(() -> new IllegalStateException("Customer dont exist!"));
    }

}

