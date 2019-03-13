package pl.fivarto.b2bplatform.appclient.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.fivarto.b2bplatform.appclient.models.UserSession;
import pl.fivarto.b2bplatform.appclient.models.services.CategoryService;

@Controller
public class RodoController {


    final UserSession userSession;
    final CategoryService categoryService;

    @Autowired
    public RodoController(UserSession userSession, CategoryService categoryService) {
        this.userSession = userSession;
        this.categoryService = categoryService;
    }

    @GetMapping("/rodo")
    public String index(Model model){
        model.addAttribute("allCategories", categoryService.getAllCategoriesWithoutParent());
        model.addAttribute("userSession", userSession);
        return "user/rodoInfo";
    }

}
