package com.github.konstantin.suspitsyn.ecommercebackend.order;

import com.github.konstantin.suspitsyn.ecommercebackend.checkout.CheckoutService;
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
    public static final String ORDER_CANCELLED_ALREADY = "Заказ был уже удален";
    public static final String EMPTY_CART = "Корзина пустая";
    private final UserService userService;
    private final ProductService productService;
    public static final int EXPIRED_DAYS_TO_ADD = 10;

    private final String UNKNOWN_USER = "anonymousUser";

    private final ProductInOrdersService productInOrdersService;
    private final OrderService orderService;
    private final CheckoutService checkoutService;

    // Orders

    private Order createOrFindOrder(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> usernameAndSession = createSessionIfNoSessionOrUsername(request, response);
        String username;
        String cookieCartId;
        User user;
        OrderStatus orderStatus = OrderStatus.CREATE;

        user = userService.returnUser(usernameAndSession);

        cookieCartId = usernameAndSession.get(COOKIE_CART_ID_NAME);

        Order order = this.findOrderByUserOrSessionAndStatus(cookieCartId, user, orderStatus);

        // If order was created by session without login
        if (order.getUser() == null && user != null) {
            orderService.updateUser(user, order.getId());
        }
        return order;
    }


    public void updateCancelOrderAnd(Order order) {

        this.cancelOrder(order.getId());

        List<ProductsInOrder> productsInOrderList = productInOrdersService.getAllByOrder(order);

        for (ProductsInOrder tempPIO : productsInOrderList) {
            Product product = tempPIO.getProduct();
            productService.updatePcs(
                    product.getUnitsInActiveStock() + tempPIO.getOrderQuantity(),
                    product.getUnitsInReserve() - tempPIO.getOrderQuantity(),
                    product.getId()
            );
        }
    }

    public Order findOrderByUserOrSessionAndStatus(String cookie, User user, OrderStatus orderStatus) {

        List<Order> presentedOrders = orderService.findOrderByUserOrCookieAndStatus(cookie, user, orderStatus);

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

            orderService.save(newOrder);

            return newOrder;
        }

        if (presentedOrders.size() > 1) {
            for (int i = 1; i < presentedOrders.size(); i++) {
                this.cancelOrder(presentedOrders.get(i).getId());
            }
        }

        return presentedOrders.get(0);
    }


    public void cancelOrder(Long orderId) {
        OrderStatus cancelledStatus = OrderStatus.CANCELED;

        Order order = orderService.getById(orderId);

        if (order.getOrderStatus() == cancelledStatus) {
            throw new IllegalStateException(ORDER_CANCELLED_ALREADY);
        }

        orderService.updateStatus(cancelledStatus, orderId);


        List<ProductsInOrder> productsInOrderList = productInOrdersService.getAllByOrder(order);

        for (ProductsInOrder tempPIO : productsInOrderList) {
            Product product = tempPIO.getProduct();
            productService.updatePcs(
                    product.getUnitsInActiveStock() + tempPIO.getOrderQuantity(),
                    product.getUnitsInReserve() - tempPIO.getOrderQuantity(),
                    product.getId()
            );
        }
    }

    // Products In Order
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

        ProductsInOrder productInOrder = productInOrdersService
                .getProductInOrder(order, productToAdd);

        if (productInOrder == null) {
            ProductsInOrder productsInOrder = new ProductsInOrder(
                    productToAdd,
                    order,
                    quantityForOrder,
                    productInOrderRequest.getPrice()
            );
            // Add product to Order
            productInOrdersService.save(productsInOrder);
        } else {
            productInOrdersService.updateQuantityAndPrice(
                    quantityForOrder + productInOrder.getOrderQuantity(),
                    productInOrderRequest.getPrice(),
                    productInOrder.getId()
            );
        }

        // Update update Order datetime
        orderService.updateUpdateDate(order.getId());

        // Update quantity and Rub in Order
        Long sumRum = order.getTotalPrice();
        Long sumUnits = order.getTotalItems();

        sumRum = sumRum + productInOrderRequest.getPrice() * productInOrderRequest.getQuantity();
        sumUnits = sumUnits + productInOrderRequest.getQuantity();

        orderService.updateSums(sumUnits, sumRum, order.getId());

    }

    public void decreaseProductAmount(HttpServletRequest request, HttpServletResponse response,
                                      Long productId, Long decreaseAmount) {

        Order order = this.createOrFindOrder(request, response);
        Product product = productService.findById(productId);

        ProductsInOrder PIO = productInOrdersService.getProductInOrder(order, product);

        Long decreaseAmountFinal = Math.min(PIO.getOrderQuantity(), decreaseAmount);
        Long price = PIO.getPrice();

        if (decreaseAmountFinal == PIO.getOrderQuantity()) {
            productInOrdersService.deleteProductInOrder(PIO);
        }

        this.changeTotalQuantityInOrder(order, -decreaseAmountFinal, price);

        productInOrdersService.updateQuantityAndPrice(PIO.getOrderQuantity() - decreaseAmountFinal,
                price, PIO.getId());

        productService.updatePcs(product.getUnitsInActiveStock() + decreaseAmountFinal,
                product.getUnitsInReserve() - decreaseAmountFinal,
                product.getId());

        order = this.createOrFindOrder(request, response);
        if (order.getTotalItems() == 0) {
            this.cancelOrder(order.getId());
        }

    }

    public void deleteProductInOrder(HttpServletRequest request, HttpServletResponse response,
                                     Long productId) {
        Order order = this.createOrFindOrder(request, response);
        Product product = productService.findById(productId);

        ProductsInOrder PIO = productInOrdersService.getProductInOrder(order, product);

        this.decreaseProductAmount(request, response, productId, -1 * PIO.getOrderQuantity());


    }

    private void changeTotalQuantityInOrder(Order order, Long amount, Long price) {
        // IMPORTANT
        // If amount > 0, then increase of quantity, else decrease

        orderService.updateSums(order.getTotalItems() + amount,
                order.getTotalPrice() + price * amount,
                order.getId());
    }

    public List<ProductsInOrder> showMyCart(HttpServletRequest request, HttpServletResponse response) {
        Order order = this.createOrFindOrder(request, response);
        return orderService.getProductsInOrder(order.getId());
    }

    public String pay(HttpServletRequest request, HttpServletResponse response) {
        Order order = this.createOrFindOrder(request, response);

        if (order.getTotalPrice() == 0) {
            throw new IllegalStateException(EMPTY_CART);
        }

        Map<String, String> paymentJson = checkoutService.sendPaymentYookassa(order.getId(), order.getTotalPrice());

        orderService.updateStatus(OrderStatus.NOT_PAID, order.getId());
        orderService.updatePaymentId(paymentJson.get("id"), order.getId());

        return paymentJson.get("confirmation_url");

    }

    public void checkAllPaymentsManually(HttpServletRequest request, HttpServletResponse response) {
        List<Order> orderList = orderService.findOrderByStatus(OrderStatus.NOT_PAID);

        if (orderList.isEmpty()) {
            return;
        }

        for (Order tempOrder: orderList) {
            Map<String, String> paymentJson = checkoutService.checkPaymentStatusYookassa(tempOrder.getPaymentId());

            if (paymentJson.get("status").equals("succeeded")) {
                orderService.updateStatus(OrderStatus.PAID, tempOrder.getId());
            }
        }
    }

    public void completeOrder(Long orderId) {
        orderService.updateStatus(OrderStatus.FINISHED, orderId);
    }


    // HELPER METHODS
    private Map<String, String> createSessionIfNoSessionOrUsername(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> usernameAndSession = new HashMap<>();
        String username;
        String cookieId = null;

        // Get username if exists
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Get cookies
        Cookie[] cookies = request.getCookies();

        // If there are some cookies
        if (cookies != null) {
            for (Cookie tempCookie : cookies) {
                if (tempCookie.getName().equals(CART_COOKIE)) {
                    cookieId = tempCookie.getValue();
                }
            }
        }

        // If cart cookie was not found
        if (cookieId == null) {
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

