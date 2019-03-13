package pl.fivarto.b2bplatform.appclient.models.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fivarto.b2bplatform.appclient.models.entites.CategoryEntity;
import pl.fivarto.b2bplatform.appclient.models.entites.ProductCategoryEntity;
import pl.fivarto.b2bplatform.appclient.models.entites.ProductEntity;
import pl.fivarto.b2bplatform.appclient.models.repotiories.CategoryRepository;
import pl.fivarto.b2bplatform.appclient.models.repotiories.ProductCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    final CategoryRepository categoryRepository;
    final ProductCategoryRepository productCategoryRepository;


    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductCategoryRepository productCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
    }


    public List<CategoryEntity> getAllCategoriesWithoutParent() {
        return categoryRepository.findAllByParentIsNullOrderByNameAsc();
    }

    public List<CategoryEntity> getCategoriesWithParent(int id) {
        return categoryRepository.findByParent_IdOrderByNameAsc(id);
    }

    public Optional<CategoryEntity> getCategoryWithId(int id) {
        return categoryRepository.findById(id);
    }


    @Transactional
    public void deleteCategory(int id) {
        List<ProductCategoryEntity> list = productCategoryRepository.findByCategory_IdOrderByProduct_NameAsc(id);
        list.forEach(s -> productCategoryRepository.deleteById(s.getId()));

        categoryRepository.deleteById(id);
    }

    public boolean updateCategory(int id, String name) {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(id);
        if (!categoryEntityOptional.isPresent()) {
            return false;
        }

        CategoryEntity categoryEntity = categoryEntityOptional.get();
        categoryEntity.setName(name);
        categoryRepository.save(categoryEntity);
        return true;
    }

    public Optional<CategoryEntity> findCategoryByName(String name) {
        return categoryRepository.findByNameEqualsIgnoreCase(name);
    }

    public CategoryEntity addCategory(String name, Integer parentId) {
        CategoryEntity categoryEntity = new CategoryEntity(name, parentId);
        return categoryRepository.save(categoryEntity);
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }


    @Transactional
    public void addOrRemoveCategoryFromProduct(int categoryId, int productId) {
        ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
        productCategoryEntity.setCategory(new CategoryEntity(categoryId));

        if (productCategoryRepository.existsByCategory_IdAndProduct_Id(categoryId, productId)) {
            productCategoryRepository.deleteByCategory_IdAndProduct_Id(categoryId, productId);
            return;
        }

        productCategoryEntity.setProduct(new ProductEntity(productId));
        productCategoryRepository.save(productCategoryEntity);
    }

    @Transactional
    public void addOrRemoveCategoryFromProducts(List<Integer> productsId, int deliveryId, int groupId) {
        productsId.forEach(s -> {
            cleanCategoriesOnProduct(s);

            if(groupId != 0)
            addOrRemoveCategoryFromProduct(groupId, s);

            if(deliveryId != 0)
            addOrRemoveCategoryFromProduct(deliveryId, s);
        });
    }

    public void cleanCategoriesOnProduct(int productId) {
        productCategoryRepository.deleteByProductId(productId);
    }

    public CategoryEntity getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalStateException("Category dont exists"));
    }

    public boolean categoryIsExistByName(String name) {
        return categoryRepository.existsByNameEqualsIgnoreCase(name);
    }

    public List<ProductEntity> getAllVisibleProductsInCategory(int categoryId) {
        return productCategoryRepository.findByCategory_IdOrderByProduct_NameAsc(categoryId).stream()
                .map(ProductCategoryEntity::getProduct)
                .filter(ProductEntity::isDisplay)
                .collect(Collectors.toList());

    }


    public Page<ProductEntity> getProductsInCategory(int categoryId) {
        return productCategoryRepository.findByCategory_IdOrderByProduct_NameAsc(categoryId, PageRequest.of(0, Integer.MAX_VALUE))
                .map(ProductCategoryEntity::getProduct);

    }


}
