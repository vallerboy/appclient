package pl.fivarto.b2bplatform.appclient.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.fivarto.b2bplatform.appclient.models.forms.LoginForm;
import pl.fivarto.b2bplatform.appclient.models.services.AuthService;

@Controller
public class LoginController {

    final AuthService loginService;

    @Autowired
    public LoginController(AuthService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "user/login";
    }

    @PostMapping({"/login"})
    public String login(@ModelAttribute("loginForm") LoginForm loginForm,
                        Model model){
        if(loginService.tryLogin(loginForm)){
            return "redirect:/";
        }

        model.addAttribute("info", "Błędne dane");
        return "user/login";
    }
}
