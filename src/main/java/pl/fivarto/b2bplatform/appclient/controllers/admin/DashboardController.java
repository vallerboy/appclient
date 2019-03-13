package pl.fivarto.b2bplatform.appclient.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/admin")
    public String dashboard() {
        return "admin/dashboard";
    }
}
