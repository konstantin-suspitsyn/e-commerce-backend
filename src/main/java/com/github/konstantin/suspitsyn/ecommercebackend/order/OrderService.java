package com.github.konstantin.suspitsyn.ecommercebackend.order;

import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    OrderRepository orderRepository;

    public List<Order> findOrderByUserOrCookieAndStatus(String cookie, User user, OrderStatus orderStatus) {
        return orderRepository.findOrderByUserOrCookieAndStatus(cookie, user, orderStatus);
    }

    public void save(Order order) {
        orderRepository.save(order);
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

    public Order getById(Long orderId) {
        return orderRepository.getById(orderId);
    }

    public void updateStatus(OrderStatus orderStatus, Long id) {
        orderRepository.updateStatus(orderStatus, id);
    }

}
