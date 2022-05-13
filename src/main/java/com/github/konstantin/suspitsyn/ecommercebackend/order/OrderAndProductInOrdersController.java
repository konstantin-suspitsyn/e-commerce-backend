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

    @GetMapping("/product/decrease")
    public void decreaseProductAmount(@RequestParam Long productId, @RequestParam Long decreaseAmount,
                           HttpServletRequest request, HttpServletResponse response) {
        ordersAndProductsInOrdersService.decreaseProductAmount(request, response,
                productId, decreaseAmount);
    }

    @GetMapping("/product/delete")
    public void deleteProductInOrder(@RequestParam Long productId,
                                      HttpServletRequest request, HttpServletResponse response) {
        ordersAndProductsInOrdersService.deleteProductInOrder(request, response, productId);
    }

    @GetMapping("/delete")
    public void cancelOrder(@RequestParam Long order) {
        ordersAndProductsInOrdersService.cancelOrder(order);
    }
}
