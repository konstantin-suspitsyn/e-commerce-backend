package com.github.konstantin.suspitsyn.ecommercebackend.order;

import com.github.konstantin.suspitsyn.ecommercebackend.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductsInOrderRepository extends JpaRepository<ProductsInOrder, Long> {

    @Query("SELECT pio " +
            "FROM ProductsInOrder pio " +
            "WHERE pio.order = ?1 " +
            "AND pio.product = ?2")
    ProductsInOrder getProductInOrder(Order order, Product product);

    @Modifying
    @Transactional
    @Query("UPDATE ProductsInOrder pio " +
            "SET pio.orderQuantity = ?1 " +
            ", pio.price = ?2 " +
            "WHERE pio.id = ?3")
    void updateQuantityAndPrice(Long quantity,
                                       Long price,
                                       Long id);


    @Query("SELECT pio " +
            "FROM ProductsInOrder pio " +
            "WHERE pio.order = ?1")
    List<ProductsInOrder> getProductsWhereOrder(Order order);
}
