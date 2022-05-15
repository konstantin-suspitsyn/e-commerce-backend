package com.github.konstantin.suspitsyn.ecommercebackend.order;

import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o " +
            "from Order o " +
            "WHERE (o.user = ?2 OR o.session = ?1) " +
            "AND o.orderStatus = ?3 " +
            "ORDER BY o.id desc")
    List<Order> findOrderByUserOrCookieAndStatus(String session, User user, OrderStatus orderStatus);

    @Query("SELECT o " +
            "from Order o " +
            "WHERE o.user = ?1")
    List<Order> findOrderByUser(User user);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Order o " +
            "SET o.orderStatus = ?1 " +
            "WHERE o.id = ?2")
    void updateStatus(OrderStatus orderStatus, Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Order o " +
            "SET o.updateDate = ?1 " +
            "WHERE o.id = ?2")
    void updateUpdateDate(LocalDateTime localDatetime, Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Order o " +
            "SET o.totalItems = ?1 " +
            ", o.totalPrice = ?2 " +
            "WHERE o.id = ?3")
    void updateSumsInOrder(Long units, Long rub, Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(("UPDATE Order o " +
            "SET o.user = ?1 " +
            "WHERE o.id = ?2"))
    void updateUser(User user, Long id);

    @Query("SELECT o.productsInOrderList " +
            "FROM Order o " +
            "WHERE o.id = ?1")
    List<ProductsInOrder> getProductsInOrder(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Order o " +
            "SET o.paymentId = ?1 " +
            "WHERE o.id = ?2")
    void updatePaymentId(String paymentId, Long id);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.orderStatus = ?1")
    List<Order> findOrderByStatus(OrderStatus orderStatus);

}
