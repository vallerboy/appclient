package pl.fivarto.b2bplatform.appclient.models.entites;

import lombok.Data;
import pl.fivarto.b2bplatform.appclient.models.forms.CustomerUpdateForm;
import pl.fivarto.b2bplatform.appclient.models.forms.RegisterForm;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer")
public class CustomerEntity {
    private @Id @GeneratedValue int id;
    private String login;
    private String password;
    private String email;
    private String nip;
    private String phone;
    private @Column(name = "is_admin") boolean isAdmin;
    private @Column(name = "company_name") String companyName;
    private @Column(name = "is_accepted") boolean isAccepted;
    private @Column(name = "registration_date") LocalDateTime registrationDate;


    public CustomerEntity(RegisterForm registerForm, boolean isAcceptedAccount){
        this.email = registerForm.getEmail();
        this.nip = registerForm.getNip();
        this.phone = registerForm.getPhoneNumber();
        this.companyName = registerForm.getCompanyName();
        this.registrationDate = LocalDateTime.now();
        setAccepted(isAcceptedAccount);
    }

    public CustomerEntity(CustomerUpdateForm customerUpdateForm){
        this.id = customerUpdateForm.getId();
        this.email = customerUpdateForm.getEmail();
        this.nip = customerUpdateForm.getNip();
        this.phone = customerUpdateForm.getPhone();
        this.companyName = customerUpdateForm.getCompanyName();
        this.password = customerUpdateForm.getPassword();
        this.login = customerUpdateForm.getLogin();
        this.isAccepted = customerUpdateForm.isAccepted();
    }
}
