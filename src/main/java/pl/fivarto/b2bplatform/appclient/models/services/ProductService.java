package pl.fivarto.b2bplatform.appclient.models.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.fivarto.b2bplatform.appclient.models.dto.ProductDto;
import pl.fivarto.b2bplatform.appclient.models.entites.ProductEntity;
import pl.fivarto.b2bplatform.appclient.models.forms.ProductUpdateForm;
import pl.fivarto.b2bplatform.appclient.models.repotiories.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final int CATEGORY_GROUP_ID = 1;
    private static final int CATEGORY_GROUP_DELIVERY_ID = 2;
    final ProductRepository productRepository;
    final CategoryService categoryService;


    @Value("50")
    int maxPageSize;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Page<ProductEntity> findAllProductsForPage(int page, String orderType) {
        String descOrAsc = null;
        String column = null;

        switch (orderType){
            case "display_true":{
                descOrAsc = "DESC";
                column = "isDisplay";
                break;
            }
            case "display_false":{
                descOrAsc = "ASC";
                column = "isDisplay";
                break;
            }
            case "count_up":{
                column = "quantity";
                descOrAsc = "ASC";
                break;
            }

            case "count_down": {
                column = "quantity";
                descOrAsc = "DESC";
                break;
            }

            case "group": {
                column = "group";
                descOrAsc = "DESC";
                break;
            }
        }

        return productRepository.findAll(PageRequest.of(page, maxPageSize,  Sort.by(Sort.Direction.fromString(descOrAsc), column)));
    }

    public Page<ProductEntity> findAllProductsForPageWhereLastEditIn(int page, int days, String orderType) {
       String descOrAsc = null;
       String column = null;

        switch (orderType){
            case "display_true":{
                descOrAsc = "DESC";
                column = "isDisplay";
                break;
            }
            case "display_false":{
                descOrAsc = "ASC";
                column = "isDisplay";
                break;
            }
            case "count_up":{
                column = "quantity";
                descOrAsc = "ASC";
                break;
            }

            case "count_down": {
                column = "quantity";
                descOrAsc = "DESC";
                break;
            }

            case "group": {
                column = "group";
                descOrAsc = "DESC";
                break;
            }
        }


        //productRepository.findAllByLastEditTimeIsAfterAndQuantityLessThanEqual(LocalDateTime.now().minusDays(180), 0f);

        return productRepository.findAllByLastEditTimeIsAfterAndIsDataEditedIsFalse(
                PageRequest.of(page, maxPageSize,  Sort.by(Sort.Direction.fromString(descOrAsc), column)),
                LocalDateTime.now().minusDays(days));

        //todo join products with last edit time after 180 days
    }



    public Page<ProductEntity> searchProductByName(String word, int page) {
        return productRepository.findAllByNameContainsIgnoreCaseOrderByName(word, PageRequest.of(page, 200));
    }

    public void updateProducts(ProductUpdateForm productUpdateForm, int id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(IllegalStateException::new);

        productEntity.setVat(productUpdateForm.getVat());
        productEntity.setPriceNetto(productUpdateForm.getPriceNetto());
        productEntity.setDisplay(productUpdateForm.isDisplay());
        productEntity.setName(productUpdateForm.getName());
        productEntity.setDataEdited(true);
        productEntity.setDescription(productUpdateForm.getDescription());

        productRepository.save(productEntity);

    }

    public ProductEntity findProductById(int id) {
        return productRepository.findById(id).orElseThrow(IllegalStateException::new);
    }

    @Async //To offhook http api request
    public void updateProducts(List<ProductDto> productDto) {

        for (ProductDto dto : productDto) {
            Optional<ProductEntity> productEntityOptional = productRepository.findByExternalId(dto.getExternalId());
            if (!productEntityOptional.isPresent()) {
                createProductInDatabaseWithCategories(dto);
                continue;
            }


            ProductEntity productEntity = productEntityOptional.get();
            checkFieldsToUpdate(productEntity, dto, productEntity.isDataEdited());
        }
    }

    private void checkFieldsToUpdate(ProductEntity productEntity, ProductDto productDto, boolean isEditedByOwner) {
        if(productEntity.getQuantity() <= 0 && productDto.getCount() > 0 && !productEntity.isDisplay()){
            productEntity.setLastEditTime(LocalDateTime.now());
        }


        productEntity.setName(productDto.getName());
        productEntity.setPriceNetto(productDto.getPriceNetto());
        productEntity.setVat(productDto.getVat());
        productEntity.setQuantity(productDto.getCount());
        productEntity.setEan(productDto.getEan());
        productEntity.setGroup(productDto.getGroup());
        productEntity.setGroupWho(productDto.getGroupWho());
        productEntity.setLastEditTime(productEntity.getLastEditTime()); //stay the same last edit data

        saveProduct(productEntity);
    }


    public Page<ProductEntity> getAllVisibleProductsInPage(int page) {
        return productRepository.findAllByIsDisplayIsTrue(PageRequest.of(page, maxPageSize));
    }

    public List<ProductEntity> searchProductsWithContainsNameAndEanAndVisible(String name) {
        return productRepository.findAllByNameContainsIgnoreCaseOrEanContainsAndIsDisplayIsTrue(name, name);
    }


    private void createProductInDatabaseWithCategories(ProductDto dto) {
        ProductEntity createdProduct = saveProduct(new ProductEntity(dto));

        List<Integer> categories = createCategories(dto);
        categories.forEach(s -> categoryService.addOrRemoveCategoryFromProduct(s, createdProduct.getId()));
    }

    private List<Integer> createCategories(ProductDto dto) {
        List<Integer> categoriesId = new ArrayList<>(2);
// było potrzebne za 1 razem
//        if(dto.getGroup().isEmpty() && dto.getGroupWho().isEmpty()){
//            return categoriesId;
//        }
//
//        if (!categoryService.categoryIsExistByName(dto.getGroup())) {
//            categoriesId.add(categoryService.addCategory(dto.getGroup(), CATEGORY_GROUP_ID).getId());
//        } else {
//            categoriesId.add(categoryService.findCategoryByName(dto.getGroup()).get().getId());
//        }
//
//        if (!categoryService.categoryIsExistByName(dto.getGroupWho())) {
//            categoriesId.add(categoryService.addCategory(dto.getGroupWho(), CATEGORY_GROUP_DELIVERY_ID).getId());
//        } else {
//            categoriesId.add(categoryService.findCategoryByName(dto.getGroupWho()).get().getId());
//        }

        return categoriesId;
    }


    public void changeDisplayState(int productId){
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException());
        productEntity.setDisplay(!productEntity.isDisplay());

        productRepository.save(productEntity);
    }

    private ProductEntity saveProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }
}
