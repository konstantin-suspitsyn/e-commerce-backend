package com.github.konstantin.suspitsyn.ecommercebackend.order;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping(path = "orders")
@RestController
@AllArgsConstructor
public class OrderAndProductInOrdersController {

    private final OrdersAndProductsInOrdersService ordersAndProductsInOrdersService;

    @GetMapping("/product/add")
    public void getSession(@RequestBody ProductInOrderRequest productInOrderRequest,
                           HttpServletRequest request, HttpServletResponse response) {
        ordersAndProductsInOrdersService.addProductToOrder(productInOrderRequest, request, response);
    }

    @GetMapping("/delete")
    public void cancelOrder(@RequestParam Long order) {
        ordersAndProductsInOrdersService.cancelOrder(order);
    }
}
