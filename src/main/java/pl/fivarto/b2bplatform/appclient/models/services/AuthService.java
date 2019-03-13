package pl.fivarto.b2bplatform.appclient.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.fivarto.b2bplatform.appclient.models.entites.CustomerEntity;
import pl.fivarto.b2bplatform.appclient.models.UserSession;
import pl.fivarto.b2bplatform.appclient.models.forms.LoginForm;
import pl.fivarto.b2bplatform.appclient.models.forms.RegisterForm;
import pl.fivarto.b2bplatform.appclient.models.repotiories.CustomerRepository;

import java.util.Optional;

@Service
public class AuthService {

    final CustomerRepository customerRepository;
    final UserSession userSession;
    final EmailService emailService;

    @Autowired
    public AuthService(CustomerRepository customerRepository, UserSession userSession, EmailService emailService) {
        this.customerRepository = customerRepository;
        this.userSession = userSession;
        this.emailService = emailService;
    }

    public boolean tryLogin(LoginForm loginForm){
         Optional<CustomerEntity> customerEntity = customerRepository.findByLoginAndPasswordAndIsAcceptedIsTrue(loginForm.getLogin(), loginForm.getPassword());
         customerEntity.ifPresent(s -> {
             userSession.setLogin(true);
             userSession.setCustomer(s);
         });

         return customerEntity.isPresent();
    }

    public boolean tryRegisterNonActiveAccount(RegisterForm registerForm) {
        if(customerRepository.existsByEmail(registerForm.getEmail())){
            return false;
        }

        CustomerEntity customerEntity = new CustomerEntity(registerForm, false);
        customerRepository.save(customerEntity);
        emailService.sendEmail(registerForm.getEmail(), EmailService.EmailType.REGISTRED);
        return true;
    }
}
