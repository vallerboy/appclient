package pl.fivarto.b2bplatform.appclient.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.fivarto.b2bplatform.appclient.models.services.CategoryService;

@Controller
public class CategoryController {


    final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/categories")
    public String users(Model model) {
        model.addAttribute("categories", categoryService.getAllCategoriesWithoutParent());
        return "admin/categoryList";
    }


    @GetMapping("/admin/category/show/{id}")
    public String showCategoriesForParent(Model model,
                        @PathVariable("id") int id) {
        model.addAttribute("categories", categoryService.getCategoriesWithParent(id));
        model.addAttribute("parentId", id);

        return "admin/categoryList";
    }

    @GetMapping("/admin/category/delete/{id}/{parent}")
    public String deleteCategory(@PathVariable("id") int id,
                                 @PathVariable("parent") Integer parentId) {
        categoryService.deleteCategory(id);
        return parentId != 0 ? "redirect:/admin/category/show/" + parentId : "redirect:/admin/categories";
    }


    @PostMapping("/admin/category/edit")
    public String editCategory(@RequestParam("id") int id,
                               @RequestParam("name") String name){
        categoryService.updateCategory(id, name);
        return "redirect:/admin/categories";
    }

    //0 = brak rodzica w parentId
    @PostMapping("/admin/category/add")
    public String addCategory(@RequestParam("name") String name,
                              @RequestParam("parentId") Integer parentId){
         categoryService.addCategory(name, parentId);
        return parentId != 0 ? "redirect:/admin/category/show/" + parentId : "redirect:/admin/categories";
    }

}
