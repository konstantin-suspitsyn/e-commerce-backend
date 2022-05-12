package com.github.konstantin.suspitsyn.ecommercebackend.order;

import com.github.konstantin.suspitsyn.ecommercebackend.product.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "products_in_order")
@NoArgsConstructor
public class ProductsInOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;
    private Long orderQuantity;
    private Long price;

    public ProductsInOrder(Product product, Order order, Long orderQuantity, Long price) {
        this.product = product;
        this.order = order;
        this.orderQuantity = orderQuantity;
        this.price = price;
    }
}
