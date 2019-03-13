package pl.fivarto.b2bplatform.appclient.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.fivarto.b2bplatform.appclient.models.forms.RegisterForm;
import pl.fivarto.b2bplatform.appclient.models.services.AuthService;

import javax.validation.Valid;

@Controller
public class RegisterController {

    final AuthService authService;

    @Autowired
    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "user/register";
    }


    @PostMapping({"/register"})
    public String register(@ModelAttribute("registerForm") @Valid RegisterForm registerForm,
                           BindingResult bindingResult,
                           Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("info", "Uzupełnij wszystkie pola" );
            return "user/register";
        }

        if(authService.tryRegisterNonActiveAccount(registerForm)){
            model.addAttribute("info", "Wniosek został wysłany. Obserwuj swoją pocztę: " + registerForm.getEmail());
            model.addAttribute("registerSuccess", true);
            return "user/register";
        }

        model.addAttribute("info", "Ten email jest zajęty");
        return "user/register";
    }

}
