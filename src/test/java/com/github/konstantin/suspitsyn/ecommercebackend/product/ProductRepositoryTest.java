package com.github.konstantin.suspitsyn.ecommercebackend.product;

import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategory;
import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
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
    public static final String SUPER_NEW_SKU = "SUPER_NEW_SKU";
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
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @Test
    void shouldGetAll() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Product> productPage = productRepository.getAll(pageable);
        assertThat(productPage.getTotalElements()).isGreaterThan(0L);
    }

    @Test
    void shouldFindByShortNameContaining() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Product> productPage = productRepository.findByShortNameContainingIgnoreCase("паук", pageable);
        assertThat(productPage.getTotalElements()).isGreaterThan(1L);
    }

    @Test
    void shouldGetWhereCategory() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Product> productPage = productRepository.getWhereCategory(productCategory, pageable);
        assertThat(productPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldUpdateQuantity() {
        productRepository.updatePcs(0L, 100L, newProduct.getId());
        Product product = productRepository.getById(newProduct.getId());
        assertThat(product.getUnitsInActiveStock()).isEqualTo(0);
        assertThat(product.getUnitsInReserve()).isEqualTo(100);
    }

    @Test
    void shouldUpdateData() {
        productRepository.updateData(SUPER_NEW_SKU, SHORT_NAME,
                DESCRIPTION,
                IMAGE_URL, LocalDate.now(), newProduct.getId());

        Product product = productRepository.getById(newProduct.getId());
        assertThat(product.getSku()).isEqualTo(SUPER_NEW_SKU);
    }

    @Test
    void shouldUpdatePrice() {
        productRepository.updatePrice(250L, newProduct.getId());
        Product product = productRepository.getById(newProduct.getId());
        assertThat(product.getUnitPrice()).isEqualTo(250L);
    }

    @Test
    void shouldChangeActive() {
        productRepository.changeActive(false, newProduct.getId());
        Product product = productRepository.getById(newProduct.getId());
        assertThat(product.getActive()).isEqualTo(false);
    }
}