package com.github.konstantin.suspitsyn.ecommercebackend.productcategories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Category Service Test")
@SpringBootTest
class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;
    private ProductCategoryService productCategoryService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        productCategoryService = new ProductCategoryService((productCategoryRepository));
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void shouldGetAllProductCategories() {
        //when
        productCategoryService.getAllProductCategories();
        //then
        Mockito.verify(productCategoryRepository).findAll();
    }

    @Test
    void shouldGetById() {

       assertThrows(IllegalStateException.class, () -> productCategoryService.getById(1L));

    }


}