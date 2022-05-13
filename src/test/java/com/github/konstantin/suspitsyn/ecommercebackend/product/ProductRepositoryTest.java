package com.github.konstantin.suspitsyn.ecommercebackend.product;

import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategory;
import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("Product Repository Test")
class ProductRepositoryTest {

    public static final String CUSTOM_CATEGORY = "Custom category";
    public static final String NEW_SKU = "NEW_SKU";
    public static final String SHORT_NAME = "shortName";
    public static final String DESCRIPTION = "description";
    public static final long PRICE_LONG = 1000000L;
    public static final String IMAGE_URL = "imageUrl";
    public static final boolean BOOLEAN_ACTIVE = true;
    public static final long IN_STOCK = 100L;
    public static final long IN_RESERVE = 0L;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    Product newProduct;
    ProductCategory productCategory;

    @BeforeEach
    void setUp() {
        productCategory = new ProductCategory(CUSTOM_CATEGORY);
        productCategoryRepository.save(productCategory);
        newProduct = new Product(
                NEW_SKU,
                SHORT_NAME,
                DESCRIPTION,
                PRICE_LONG,
                IMAGE_URL,
                BOOLEAN_ACTIVE,
                IN_STOCK,
                IN_RESERVE,
                LocalDate.now(),
                null,
                productCategory);
        productRepository.save(newProduct);
    }

    @AfterEach
    void tearDown() {
        productCategoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void getAll() {
    }

    @Test
    void findByShortNameContaining() {
    }

    @Test
    void getWhereCategory() {
    }

    @Test
    void updateQuantity() {
    }

    @Test
    void updateData() {
    }

    @Test
    void updatePrice() {
    }

    @Test
    void changeActive() {
    }
}