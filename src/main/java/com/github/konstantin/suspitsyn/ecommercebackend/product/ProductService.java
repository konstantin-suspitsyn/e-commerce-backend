package com.github.konstantin.suspitsyn.ecommercebackend.product;

import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategory;
import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;


@Service
@AllArgsConstructor
public class ProductService {

    public static final String NO_CATEGORY = "Категория не существует";
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;

    public Page<Product> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findAll(pageable);
    }


    public Page<Product> findByShortNameContaining(String searchName,
                                                   Integer pageNo,
                                                   Integer perPage) {
        Pageable pageable = PageRequest.of(pageNo, perPage);
        return productRepository.findByShortNameContainingIgnoreCase(searchName, pageable);
    }

    public Product createNewProduct(ProductCreateRequest productCreateRequest, HttpServletRequest request) {

        if (!productCategoryService.categoryPresent(productCreateRequest.getCategory())) {
            throw new IllegalStateException(NO_CATEGORY);
        }

        Product product = new Product(
                productCreateRequest.getSku(),
                productCreateRequest.getShortName(),
                productCreateRequest.getDescription(),
                productCreateRequest.getUnitPrice(),
                productCreateRequest.getImageUrl(),
                productCreateRequest.getActive(),
                productCreateRequest.getUnitsInActiveStock(),
                productCreateRequest.getUnitsInReserve(),
                LocalDate.now(),
                null,
                productCategoryService.getById(productCreateRequest.getCategory())
        );
        productRepository.save(product);
        return product;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void changeEnabled(Long id) {
        Product product = productRepository.getById(id);

    }

    public Page<Product> filterByCategory(Long categoryId, Integer pageNo, Integer perPage) {
        if (!productCategoryService.categoryPresent(categoryId)) {
            throw new IllegalStateException(NO_CATEGORY);
        }
        ProductCategory productCategory = productCategoryService.getById(categoryId);
        Pageable pageable = PageRequest.of(pageNo, perPage);
        return productRepository.getWhereCategory(productCategory, pageable);
    }


    public String getPrice(Long id) {
        return String.valueOf(productRepository.getById(id).getUnitPrice());
    }

    public Product findById(Long id) {
        return productRepository.getById(id);
    }

    public void updatePcs(Long activeStock, Long reservedStock, Long id) {
        productRepository.updatePcs(activeStock, reservedStock, id);
    }

}
