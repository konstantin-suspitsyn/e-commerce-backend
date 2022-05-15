package com.github.konstantin.suspitsyn.ecommercebackend.order;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping(path = "orders")
@RestController
@AllArgsConstructor
public class OrderAndProductInOrdersController {

    private final OrdersAndProductsInOrdersService ordersAndProductsInOrdersService;

    @PostMapping("/product/add")
    public void addProduct(@RequestBody ProductInOrderRequest productInOrderRequest,
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

    @GetMapping("/my-cart")
    public List<ProductsInOrder> showMyCart(HttpServletRequest request, HttpServletResponse response) {
        return ordersAndProductsInOrdersService.showMyCart(request, response);
    }

    @GetMapping("/pay")
    public String pay(HttpServletRequest request, HttpServletResponse response) {
        return ordersAndProductsInOrdersService.pay(request, response);
    }

    @GetMapping("/check_payments_manually")
    public void checkAllPaymentsManually(HttpServletRequest request, HttpServletResponse response) {
        ordersAndProductsInOrdersService.checkAllPaymentsManually(request, response);
    }

}
