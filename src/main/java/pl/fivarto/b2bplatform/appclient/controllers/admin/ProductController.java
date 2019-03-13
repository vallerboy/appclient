package pl.fivarto.b2bplatform.appclient.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.fivarto.b2bplatform.appclient.models.forms.ProductUpdateForm;
import pl.fivarto.b2bplatform.appclient.models.services.CategoryService;
import pl.fivarto.b2bplatform.appclient.models.services.ProductImageService;
import pl.fivarto.b2bplatform.appclient.models.services.ProductService;
import retrofit2.http.Path;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



@Controller
public class ProductController {

    final ProductService productService;
    final ProductImageService productImageService;
    final CategoryService categoryService;


    @Autowired
    public ProductController(ProductService productService, ProductImageService productImageService, CategoryService categoryService) {
        this.productService = productService;
        this.productImageService = productImageService;
        this.categoryService = categoryService;
    }


    @GetMapping("/admin/products/{page}")
    public String products(@PathVariable(value = "page", required = false) int page,
                           Model model) {
        model.addAttribute("productsPage", productService.findAllProductsForPage(page));
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return "admin/productList";
    }

    @GetMapping("/admin/products/search/{word}")
    public String productsSearch(@PathVariable(value = "word", required = false) String word,
                                 Model model) {
        if (word.isEmpty()) {
            return "redirect:/admin/products/0";
        }
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("typedWord", word);
        model.addAttribute("productsPage", productService.searchProductByName(word, 0));
        return "admin/productList";
    }

    @GetMapping("/admin/products/category/{id}")
    public String productsCategory(@PathVariable(value = "id", required = true) int id,
                                   Model model) {
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("productsPage", categoryService.getProductsInCategory(id));
        return "admin/productList";
    }


    @GetMapping("/admin/product/edit/{id}")
    public String editOneProduct(@PathVariable("id") int id,
                                 Model model) {
        model.addAttribute("productUpdateForm", productService.findProductById(id));
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return "admin/product";
    }

    @GetMapping("/admin/product/edit/category/{categoryId}/{productId}")
    public String editOneProduct(@PathVariable("categoryId") int categoryId,
                                 @PathVariable("productId") int productId) {
        categoryService.addOrRemoveCategoryFromProduct(categoryId, productId);
        return "redirect:/admin/product/edit/" + productId;
    }

    @PostMapping("/admin/product/edit/{id}")
    public String getDataToEdit(@PathVariable("id") int id,
                                @ModelAttribute ProductUpdateForm productUpdateForm) {
        productService.updateProducts(productUpdateForm, id);
        return "redirect:/admin/product/edit/" + id;
    }

    @PostMapping("/admin/product/edit/image/{id}")
    public String editProductImage(@RequestParam("image") MultipartFile image,
                                   @PathVariable("id") int id) {
        try {
            if (!image.isEmpty())
                productImageService.uploadOrChangeImage(id, image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/product/edit/" + id;
    }


    @PostMapping("/admin/product/change/display/{id}")
    public String changeDisplay(@PathVariable("id") int id) {
        productService.changeDisplayState(id);
        return "redirect:/admin/products/0";
    }


    @GetMapping("/admin/products/lastedit/{page}/{sort}")
    public String productsLastedit(@PathVariable(value = "page", required = false) int page,
                                   @PathVariable(value = "sort", required = false) String sortType,
                           Model model) {
        if(sortType == null || sortType.isEmpty()){
            sortType = "group";
        }

        model.addAttribute("sort", sortType);
        model.addAttribute("page", page);
        model.addAttribute("productsPage", productService.findAllProductsForPageWhereLastEditIn(page, 14, sortType));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/productNewList";
    }


    @PostMapping("/admin/product/change/category/{groupId}/{deliveryId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String changeCategories(@PathVariable("groupId") int groupId,
                                   @PathVariable("deliveryId") int deliveryId,
                                   @RequestBody List<Integer> productsId) {

        System.out.println(productsId);
        System.out.println(groupId);
        System.out.println(deliveryId);
        categoryService.addOrRemoveCategoryFromProducts(productsId, deliveryId, groupId);
        return "OK";
    }


    //clearfix
    @GetMapping("/products/search/")
    public String productsSearchEnd() {
        return "redirect:/products/0";
    }



}
