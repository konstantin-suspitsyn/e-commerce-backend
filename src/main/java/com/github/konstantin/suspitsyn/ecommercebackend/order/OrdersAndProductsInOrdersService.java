package com.github.konstantin.suspitsyn.ecommercebackend.order;

import com.github.konstantin.suspitsyn.ecommercebackend.product.Product;
import com.github.konstantin.suspitsyn.ecommercebackend.product.ProductService;
import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrdersAndProductsInOrdersService {

    public static final String CART_COOKIE = "cartId";
    public static final String COOKIE_CART_ID_NAME = "cookieCartId";
    public static final String INACTIVE_GOODS = "Товар заказать нельзя. Он неактивный";
    public static final String SHORTAGE = "Товар на складе закончился";
    public static final String ORDER_CANCELLED_ALREADY = "Заказ был уже удолен";
    private final ProductsInOrderRepository productsInOrderRepository;
    private final UserService userService;
    private final ProductService productService;
    public static final int EXPIRED_DAYS_TO_ADD = 10;
    private final OrderRepository orderRepository;

    private final String UNKNOWN_USER = "anonymousUser";

    // Orders

    private Order createOrFindOrder(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> usernameAndSession = createSessionIfNoSessionOrUsername(request, response);
        String username;
        String cookieCartId;
        User user;
        OrderStatus orderStatus = OrderStatus.CREATE;

        // If user is not logged in
        if (usernameAndSession.get("username") == UNKNOWN_USER) {
            user = null;
        } else {
            username = usernameAndSession.get("username");
            user = userService.findByEmail(username);
        }

        cookieCartId = usernameAndSession.get(COOKIE_CART_ID_NAME);

        Order order = this.findOrderByUserOrSessionAndStatus(cookieCartId, user, orderStatus);

        // If order was created by session without login
        if (order.getUser() == null && user != null) {
            this.updateUser(user, order.getId());
        }

        return order;
    }

    public void addProductToOrder(ProductInOrderRequest productInOrderRequest,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        Long quantityForOrder;

        Order order = createOrFindOrder(request, response);
        Product productToAdd = productService.findById(productInOrderRequest.getId());

        if (!productToAdd.getActive()) {
            throw new IllegalStateException(INACTIVE_GOODS);
        }

        // Order amount
        quantityForOrder = Math.min(productToAdd.getUnitsInActiveStock(),
                productInOrderRequest.getQuantity());

        if (quantityForOrder == 0) {
            throw new IllegalStateException(SHORTAGE);
        }

        productService.updatePcs(
                productToAdd.getUnitsInActiveStock() - quantityForOrder,
                productToAdd.getUnitsInReserve() + quantityForOrder,
                productToAdd.getId()
        );

        ProductsInOrder productInOrder = productsInOrderRepository
                .getProductInOrder(order, productToAdd);

        if (productInOrder == null) {
            ProductsInOrder productsInOrder = new ProductsInOrder (
                    productToAdd,
                    order,
                    quantityForOrder,
                    productInOrderRequest.getPrice()
            );
            // Add product to Order
            productsInOrderRepository.save(productsInOrder);
        } else {
            this.updateQuantityAndPrice(
                    productInOrderRequest.getQuantity() + productInOrder.getOrderQuantity(),
                    productInOrderRequest.getPrice(),
                    productInOrder.getId()
            );
        }

        // Update update Order datetime
        this.updateUpdateDate(order.getId());

        // Update quantity and Rub in Order
        Long sumRum = order.getTotalPrice();
        Long sumUnits = order.getTotalItems();

        sumRum = sumRum + productInOrderRequest.getPrice() * productInOrderRequest.getQuantity();
        sumUnits = sumUnits + productInOrderRequest.getQuantity();

        this.updateSums(sumUnits, sumRum, order.getId());

    }

    public void updateQuantityAndPrice(Long quantity, Long price, Long id) {
        productsInOrderRepository.updateQuantityAndPrice(quantity, price, id);
    }

    public List<ProductsInOrder> getAllByOrder(Order order) {
        return productsInOrderRepository.getProductsWhereOrder(order);
    }

    public void updateCancelOrderAnd (Order order) {

        this.cancelOrder(order.getId());

        List<ProductsInOrder> productsInOrderList = this.getAllByOrder(order);

        for(ProductsInOrder tempPIO: productsInOrderList) {
            Product product = tempPIO.getProduct();
            productService.updatePcs(
                    product.getUnitsInActiveStock() + tempPIO.getOrderQuantity(),
                    product.getUnitsInReserve() - tempPIO.getOrderQuantity(),
                    product.getId()
            );
        }
    }

    public Order findOrderByUserOrSessionAndStatus(String cookie, User user, OrderStatus orderStatus) {

        List<Order> presentedOrders = orderRepository.findOrderByUserOrSessionAndStatus(cookie, user, orderStatus);

        if (presentedOrders.isEmpty()) {
            // Creating new order
            Order newOrder = new Order(
                    user,
                    cookie,
                    0L,
                    0L,
                    OrderStatus.CREATE,
                    LocalDateTime.now(),
                    LocalDate.now().plusDays(EXPIRED_DAYS_TO_ADD),
                    null
            );

            orderRepository.save(newOrder);

            return newOrder;
        }

        if (presentedOrders.size() > 1) {
            for (int i = 1; i < presentedOrders.size(); i++) {
                this.cancelOrder(presentedOrders.get(i).getId());
            }
        }

        return presentedOrders.get(0);
    }

    public void updateUpdateDate(Long id) {
        orderRepository.updateUpdateDate(LocalDateTime.now(), id);
    }

    public void updateSums(Long units, Long rub, Long id) {
        // It doesn't add or subtract, you should do it on your own
        orderRepository.updateSumsInOrder(units, rub, id);
    }

    public void updateUser(User user, Long id) {
        orderRepository.updateUser(user, id);
    }

    public List<ProductsInOrder> getProductsInOrder(Long id) {
        return orderRepository.getProductsInOrder(id);
    }

    public void cancelOrder(Long orderId) {
        OrderStatus cancelledStatus = OrderStatus.CANCELED;

        Order order = orderRepository.getById(orderId);

        if (order.getOrderStatus() == cancelledStatus) {
            throw new IllegalStateException(ORDER_CANCELLED_ALREADY);
        }

        orderRepository.updateStatus(cancelledStatus, orderId);


        List<ProductsInOrder> productsInOrderList = this.getAllByOrder(order);

        for(ProductsInOrder tempPIO: productsInOrderList) {
            Product product = tempPIO.getProduct();
            productService.updatePcs(
                    product.getUnitsInActiveStock() + tempPIO.getOrderQuantity(),
                    product.getUnitsInReserve() - tempPIO.getOrderQuantity(),
                    product.getId()
            );
        }

    }

    // HELPER METHODS
    private Map<String, String> createSessionIfNoSessionOrUsername(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> usernameAndSession = new HashMap<>();
        String username;
        String cookieId = null;

        // Get username if exists
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Get cookies
        Cookie[] cookies = request.getCookies();

        // If there are some cookies
        if (cookies != null) {
            for(Cookie tempCookie: cookies) {
                if (tempCookie.getName().equals(CART_COOKIE)) {
                    cookieId = tempCookie.getValue();
                }
            }
        }

        // If cart cookie was not found
        if (cookieId==null) {
            cookieId = setCookie(response);
        }

        usernameAndSession.put("username", username);
        usernameAndSession.put(COOKIE_CART_ID_NAME, cookieId);

        return usernameAndSession;
    }

    private String setCookie(HttpServletResponse response) {
        String cookieId = UUID.randomUUID().toString();
        Cookie theCookie = new Cookie(CART_COOKIE, cookieId);
        theCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(theCookie);
        return cookieId;
    }
}