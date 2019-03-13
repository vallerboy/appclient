package pl.fivarto.b2bplatform.appclient.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.fivarto.b2bplatform.appclient.models.entites.CustomerEntity;
import pl.fivarto.b2bplatform.appclient.models.forms.CustomerUpdateForm;
import pl.fivarto.b2bplatform.appclient.models.repotiories.CustomerRepository;

import java.util.Optional;

@Service
public class CustomerService {

    final CustomerRepository customerRepository;
    final EmailService emailService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, EmailService emailService) {
        this.customerRepository = customerRepository;
        this.emailService = emailService;
    }


    public Iterable<CustomerEntity> getAllCustomers() {
        return  customerRepository.findAllByOrderByRegistrationDateDesc();
    }

    public Optional<CustomerEntity> getCustomerById(int id){
        return  customerRepository.findById(id);
    }

    public boolean updateCustomer(CustomerUpdateForm customerUpdateForm) {
        Optional<CustomerEntity> properCustomer = customerRepository.findByLogin(customerUpdateForm.getLogin());
        if(properCustomer.isPresent()){
            if(properCustomer.get().getId() != customerUpdateForm.getId()){
                return false; //if login is busy then return update
            }
        }

        Optional<CustomerEntity> currentEditedCustomer = customerRepository.findById(customerUpdateForm.getId());
        if(currentEditedCustomer.isPresent() && currentEditedCustomer.get().isAccepted() != customerUpdateForm.isAccepted()){
            if(customerUpdateForm.isAccepted()){
                emailService.sendEmail(customerUpdateForm.getEmail(), EmailService.EmailType.ACCOUNT_ACTIVATED);
            }else{
                emailService.sendEmail(customerUpdateForm.getEmail(), EmailService.EmailType.ACCOUNT_DEACTIVATED);
            }
        }

        customerRepository.save(new CustomerEntity(customerUpdateForm));
        return true;
    }


}
