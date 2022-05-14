package com.github.konstantin.suspitsyn.ecommercebackend.order;

import com.github.konstantin.suspitsyn.ecommercebackend.product.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductInOrdersService {

    ProductsInOrderRepository productsInOrderRepository;

    public ProductsInOrder getProductInOrder(Order order, Product product) {
        return productsInOrderRepository.getProductInOrder(order, product);
    }

    public void save(ProductsInOrder productsInOrder) {
        productsInOrderRepository.save(productsInOrder);
    }

    public void updateQuantityAndPrice(Long quantity, Long price, Long id) {
        productsInOrderRepository.updateQuantityAndPrice(quantity, price, id);
    }

    public List<ProductsInOrder> getAllByOrder(Order order) {
        return productsInOrderRepository.getProductsWhereOrder(order);
    }

    public void deleteProductInOrder(ProductsInOrder productsInOrder) {
        productsInOrderRepository.delete(productsInOrder);
    }

}
