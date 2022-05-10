package com.github.konstantin.suspitsyn.ecommercebackend.productcategories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("Product Category Repository Test")
class ProductCategoryRepositoryTest {

    public static final String BEST_IN_THE_WORLD_PRODUCTS = "Best in the world products";
    public static final String NEW_BEST_IN_THE_WORLD_PRODUCTS = "NEW Best in the world products";
    ProductCategory productCategory;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @BeforeEach
    void setUp() {
        productCategory = new ProductCategory(BEST_IN_THE_WORLD_PRODUCTS);
        productCategoryRepository.save(productCategory);
    }

    @AfterEach
    void tearDown() {
        productCategoryRepository.deleteAll();
    }

    @Test
    void shouldFindById() {
        ProductCategory productCategoryNew = productCategoryRepository
                .findById(productCategory.getId())
                .orElseThrow(() -> new IllegalStateException("IllegalStateException"));

        assertThat(productCategoryNew.getName()).isEqualTo(BEST_IN_THE_WORLD_PRODUCTS);


    }

    @Test
    void updateName() {
        productCategoryRepository
                .updateName(NEW_BEST_IN_THE_WORLD_PRODUCTS, productCategory.getId());
    }
}