package com.github.konstantin.suspitsyn.ecommercebackend.order;

import com.github.konstantin.suspitsyn.ecommercebackend.product.Product;
import com.github.konstantin.suspitsyn.ecommercebackend.product.ProductRepository;
import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategory;
import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategoryRepository;
import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRepository;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
@DisplayName("Order And Product In Order User Experience Test")
public class OrderAndProductInOrderUserExperienceTest {
    // We create test user, two test products, will add products to order,
    // add quantity, remove quantity, delete products, cancel order


    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    ProductsInOrderRepository productsInOrderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    ProductCategory testCategory;
    private final String TEST_CATEGORY = "TEST_CATEGORY";


    Product testProductOne;
    public static final String NEW_SKU_1 = "testSkuOne";
    public static final String SHORT_NAME_1 = "shortName";
    public static final String DESCRIPTION_1 = "description";
    public static final long PRICE_LONG_1 = 500L;
    public static final String IMAGE_URL_1 = "imageUrl";
    public static final boolean BOOLEAN_ACTIVE_1 = true;
    public static final long IN_STOCK_1 = 100L;
    public static final long IN_RESERVE_1 = 0L;

    Product testProductTwo;
    public static final String NEW_SKU_2 = "testSkuTwo";
    public static final String SHORT_NAME_2 = "shortName";
    public static final String DESCRIPTION_2 = "description";
    public static final long PRICE_LONG_2 = 500L;
    public static final String IMAGE_URL_2 = "imageUrl";
    public static final boolean BOOLEAN_ACTIVE_2 = true;
    public static final long IN_STOCK_2 = 100L;
    public static final long IN_RESERVE_2 = 0L;

    User testUser;
    private static final String FIRST_NAME = "Имя";
    private static final String LAST_NAME = "Фамилия";
    private static final String EMAIL = "testuser@testuser.ru";
    private static final String PASSWORD = "password";
    private static final boolean LOCKED = false;
    private static final boolean ENABLED = true;

    Order order;

    @BeforeEach
    void setUp() {
        // Add Test Category
        testCategory = new ProductCategory(TEST_CATEGORY);
        productCategoryRepository.save(testCategory);

        // Add products
        testProductOne = new Product(NEW_SKU_1, SHORT_NAME_1, DESCRIPTION_1, PRICE_LONG_1,
                IMAGE_URL_1, BOOLEAN_ACTIVE_1, IN_STOCK_1, IN_RESERVE_1, LocalDate.now(), null,
                testCategory);
        testProductTwo = new Product(NEW_SKU_2, SHORT_NAME_2, DESCRIPTION_2, PRICE_LONG_2,
                IMAGE_URL_2, BOOLEAN_ACTIVE_2, IN_STOCK_2, IN_RESERVE_2, LocalDate.now(), null,
                testCategory);
        productRepository.save(testProductOne);
        productRepository.save(testProductTwo);

        testUser = new User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, UserRole.USER,
                LOCKED,ENABLED);
        userRepository.save(testUser);

    }

    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
        productRepository.delete(testProductOne);
        productRepository.delete(testProductTwo);
        productCategoryRepository.delete(testCategory);
    }

    @Test
    void name() {
    }
}
