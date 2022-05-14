package com.github.konstantin.suspitsyn.ecommercebackend.order;

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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Order Repository Test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    private User user;
    private String session = "flaksjdflaksjdf;laksdjf;laksjfd";
    private Long totalPrice = 0L;
    private Long totalItems = 0L;
    private OrderStatus orderStatus = OrderStatus.CREATE;
    private LocalDateTime creationDate = LocalDateTime.now();
    private LocalDate expirationDate = LocalDate.now().plusDays(10);
    private LocalDateTime updateDate = null;

    private OrderStatus newStatus = OrderStatus.CANCELED;

    private static final String FIRST_NAME = "Имя";
    private static final String LAST_NAME = "Фамилия";
    private static final String EMAIL = "mail@mail.ru";
    private static final String PASSWORD = "password";
    private static final boolean LOCKED = false;
    private static final boolean ENABLED = false;

    private Order order;


    @BeforeEach
    void setUp() {

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
    }

    @AfterEach
    void tearDown() {
        orderRepository.delete(order);
        userRepository.delete(user);
    }

    @Test
    void shouldFindOrderByUserOrCookieAndStatus() {
        List<Order> newOrderList = orderRepository.findOrderByUserOrCookieAndStatus(session, null, orderStatus);
        Order newOrder = newOrderList.get(0);
        assertThat(newOrder.getExpirationDate()).isEqualTo(expirationDate);
    }

    @Test
    void shouldFindOrderByUser() {
        List<Order> newOrderList = orderRepository.findOrderByUser(user);
        Order newOrder = newOrderList.get(0);
        assertThat(newOrder.getSession()).isEqualTo(session);
    }

    @Test
    void shouldUpdateStatus() {
        Long orderId = order.getId();
        orderRepository.updateStatus(newStatus, orderId);
        Order newOrderA = orderRepository.getById(order.getId());
        assertThat(newOrderA.getOrderStatus()).isEqualTo(newStatus);
    }

    @Test
    void shouldUpdateUpdateDate() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);;
        orderRepository.updateUpdateDate(localDateTime, order.getId());
        Order newOrder = orderRepository.getById(order.getId());
        assertThat(newOrder.getUpdateDate()).isEqualTo(localDateTime);
    }

    @Test
    void shouldUpdateSumsInOrder() {
        orderRepository.updateSumsInOrder(500L, 1500L, order.getId());
        Order newOrderA = orderRepository.getById(order.getId());
        assertThat(newOrderA.getTotalItems()).isEqualTo(500L);
        assertThat(newOrderA.getTotalPrice()).isEqualTo(1500L);
    }

    @Test
    void shouldUpdateUser() {
        User userN = new User(
                FIRST_NAME,
                LAST_NAME,
                EMAIL+"aa",
                PASSWORD,
                UserRole.USER,
                LOCKED,
                ENABLED
        );
        userRepository.save(userN);
        orderRepository.updateUser(userN, order.getId());
        Order newOrderA = orderRepository.getById(order.getId());
        assertThat(newOrderA.getUser().getEmail()).isEqualTo(EMAIL+"aa");
        userRepository.delete(userN);
    }

    @Test
    void shouldGetProductsInOrder() {
        List<ProductsInOrder> productsInOrderList = orderRepository.getProductsInOrder(order.getId());
        assertThat(productsInOrderList.isEmpty()).isEqualTo(true);
    }
}