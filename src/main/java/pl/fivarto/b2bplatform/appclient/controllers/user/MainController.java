package pl.fivarto.b2bplatform.appclient.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.fivarto.b2bplatform.appclient.models.UserSession;
import pl.fivarto.b2bplatform.appclient.models.entites.CategoryEntity;
import pl.fivarto.b2bplatform.appclient.models.entites.ProductEntity;
import pl.fivarto.b2bplatform.appclient.models.services.CategoryService;
import pl.fivarto.b2bplatform.appclient.models.services.OrderService;
import pl.fivarto.b2bplatform.appclient.models.services.ProductService;

@Controller
public class MainController {

    final UserSession userSession;
    final CategoryService categoryService;
    final OrderService orderService;
    final ProductService productService;

    @Autowired
    public MainController(UserSession userSession, CategoryService categoryService, OrderService orderService, ProductService productService) {
        this.userSession = userSession;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @ModelAttribute
    public void addOrderToAllRequest(Model model){
        model.addAttribute("order", orderService.getOrCreateLastOrderForUser());
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("allCategories", categoryService.getAllCategoriesWithoutParent());
        return "user/index";
    }


    @GetMapping("/{page}")
    public String index(Model model,
                        @PathVariable("page") int page) {
        model.addAttribute("allCategories", categoryService.getAllCategoriesWithoutParent());

        Page<ProductEntity> pageWithProducts  = productService.getAllVisibleProductsInPage(page);
        model.addAttribute("products", pageWithProducts.getContent());
        model.addAttribute("page", pageWithProducts); //todo bleee

        return "user/index";
    }

    @GetMapping("/search/category/{id}")
    public String indexWithCategory(@PathVariable("id") int id,
                                    Model model) {
         model.addAttribute("allCategories", categoryService.getAllCategoriesWithoutParent());
         model.addAttribute("products", categoryService.getAllVisibleProductsInCategory(id));


        CategoryEntity downloadedCategory = categoryService.getCategoryById(id);

        model.addAttribute("activeCategoryParent", downloadedCategory.getParent());
        model.addAttribute("activeCategoryId", id);
        model.addAttribute("activeCategoryName", downloadedCategory.getName());
        return "user/index";
    }


    @GetMapping("/search/product/{name}")
    public String indexWithProduct(@PathVariable("name") String name,
                                    Model model) {
        model.addAttribute("allCategories", categoryService.getAllCategoriesWithoutParent());
        model.addAttribute("products", productService.searchProductsWithContainsNameAndEanAndVisible(name));
        model.addAttribute("typedWord", name);
        model.addAttribute("page", null);
        return "user/index";
    }

    @GetMapping("/search/product")
    public String indexWithProduct(Model model) {
        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout() {
        userSession.logout();
        return "redirect:/login";
    }

    @GetMapping("/transaction/get/cart/count")
    @ResponseBody
    public String getCartItemCount() {
        return String.valueOf(orderService.getOrCreateLastOrderForUser().getOrderItems().size());
    }

}
