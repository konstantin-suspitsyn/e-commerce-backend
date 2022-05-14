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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Products In Order Repository Test")
class ProductsInOrderRepositoryTest {

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductsInOrderRepository productsInOrderRepository;


    public User user;
    public String session = "flaksjdflaksjdf;laksdjf;laksjfd";
    public Long totalPrice = 0L;
    public Long totalItems = 0L;
    public OrderStatus orderStatus = OrderStatus.CREATE;
    public LocalDateTime creationDate = LocalDateTime.now();
    public LocalDate expirationDate = LocalDate.now().plusDays(10);
    public LocalDateTime updateDate = null;

    public static final String FIRST_NAME = "Имя";
    public static final String LAST_NAME = "Фамилия";
    public static final String EMAIL = "mail@mail.ru";
    public static final String PASSWORD = "password";
    public static final boolean LOCKED = false;
    public static final boolean ENABLED = false;

    public static final String CUSTOM_CATEGORY = "Custom category";
    public static final String NEW_SKU = "NEW_SKU";
    public static final String SHORT_NAME = "shortName";
    public static final String DESCRIPTION = "description";
    public static final long PRICE_LONG = 1000000L;
    public static final String IMAGE_URL = "imageUrl";
    public static final boolean BOOLEAN_ACTIVE = true;
    public static final long IN_STOCK = 100L;
    public static final long IN_RESERVE = 0L;

    public Long units = 100L;
    public Long price = 1500L;

    public Product newProduct;
    public ProductCategory productCategory;
    public Order order;
    public ProductsInOrder productsInOrder;

    @BeforeEach
    void setUp() {

        productCategory = new ProductCategory(CUSTOM_CATEGORY);

        this.productCategoryRepository.save(productCategory);

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

        user = new User(
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                PASSWORD,
                UserRole.USER,
                LOCKED,
                ENABLED
        );

        userRepository.save(user);

        order = new Order(
                user,
                session,
                totalPrice,
                totalItems,
                orderStatus,
                creationDate,
                expirationDate,
                null
        );
        orderRepository.save(order);

        productsInOrder = new ProductsInOrder(
                newProduct,
                order,
                units,
                price
        );

        productsInOrderRepository.save(productsInOrder);

    }

    @AfterEach
    void tearDown() {

        productsInOrderRepository.delete(productsInOrder);

        orderRepository.delete(order);

        userRepository.delete(user);

        productRepository.delete(newProduct);

        productCategoryRepository.delete(productCategory);

    }

    @Test
    void shouldGetProductInOrder() {
        ProductsInOrder productsInOrder = productsInOrderRepository.getProductInOrder(order, newProduct);
        assertThat(productsInOrder.getProduct().getId()).isEqualTo(newProduct.getId());
    }

    @Test
    void shouldUpdateQuantityAndPrice() {
        productsInOrderRepository.updateQuantityAndPrice(2500L,
                1900L,
                productsInOrder.getId());

        ProductsInOrder productsInOrderNew = productsInOrderRepository.getById(productsInOrder.getId());
        assertThat(productsInOrderNew.getOrderQuantity()).isEqualTo(2500L);
        assertThat(productsInOrderNew.getPrice()).isEqualTo(1900L);

    }

    @Test
    void shouldGetProductsWhereOrder() {
        List<ProductsInOrder> productsInOrderList = productsInOrderRepository.getProductsWhereOrder(order);
        assertThat(productsInOrderList.get(0).getId()).isEqualTo(productsInOrder.getId());
    }
}