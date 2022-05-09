package com.github.konstantin.suspitsyn.ecommercebackend.productcategories;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductCategoryService {

    public static final String CATEGORY_NOT_FOUND = "Категория не найдена";
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    public ProductCategory getById(Long id) {
        return productCategoryRepository
                .findById(id).orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND));
    }


    public ProductCategory createProductCategory(String name, HttpServletRequest request, HttpServletResponse response) {
        ProductCategory productCategory = new ProductCategory(name);
        productCategoryRepository.save(productCategory);
        return productCategory;
    }

    public void updateProductCategory(String name,
                                                 Long id,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        if (!productCategoryRepository.findById(id).isPresent()) {
            throw new IllegalStateException(CATEGORY_NOT_FOUND);
        }
        productCategoryRepository.updateName(name, id);
    }

    public void deleteProductCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }
}
