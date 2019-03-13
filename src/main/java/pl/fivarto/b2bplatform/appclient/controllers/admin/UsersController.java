package pl.fivarto.b2bplatform.appclient.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.fivarto.b2bplatform.appclient.models.entites.CustomerEntity;
import pl.fivarto.b2bplatform.appclient.models.forms.CustomerUpdateForm;
import pl.fivarto.b2bplatform.appclient.models.services.CustomerService;

import java.util.Optional;

@Controller
public class UsersController {


    final CustomerService customerService;

    @Autowired
    public UsersController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("customerList", customerService.getAllCustomers());
        return "admin/userList";
    }


    @GetMapping("/admin/user/{id}")
    public String user(@PathVariable("id") int id,
                       Model model) {
        Optional<CustomerEntity> customerEntityOptional = customerService.getCustomerById(id);

        model.addAttribute("customer", customerEntityOptional.get());
        model.addAttribute("orderCount", 0); //todo order count
        model.addAttribute("customerUpdateForm",customerEntityOptional.get());
        return "admin/user";
    }

    @PostMapping("/admin/user")
    public  String updateUser(@ModelAttribute("customerUpdateForm") CustomerUpdateForm customerUpdateForm){
            if(customerService.updateCustomer(customerUpdateForm)) {
                return "redirect:/admin/users";
            }
            return "redirect:/admin/user/" + customerUpdateForm.getId();
    }
}
