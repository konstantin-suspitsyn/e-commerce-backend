package com.github.konstantin.suspitsyn.ecommercebackend.order;

import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    private String session;
    private Long totalPrice;
    private Long totalItems;
    @Enumerated
    private OrderStatus orderStatus;
    private LocalDateTime creationDate;
    private LocalDate expirationDate;
    private LocalDateTime updateDate;
    @OneToMany(mappedBy = "order")
    List<ProductsInOrder> productsInOrderList;

    public Order(User user,
                 String session,
                 Long totalPrice,
                 Long totalItems,
                 OrderStatus orderStatus,
                 LocalDateTime creationDate,
                 LocalDate expirationDate,
                 LocalDateTime updateDate) {
        this.user = user;
        this.session = session;
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
        this.orderStatus = orderStatus;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.updateDate = updateDate;
    }
}
