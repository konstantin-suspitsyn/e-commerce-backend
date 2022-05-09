package com.github.konstantin.suspitsyn.ecommercebackend.productcategories;

import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE ProductCategory pc " +
            "SET pc.name = ?1 " +
            "WHERE pc.id = ?2")
    void updateName(String name, Long id);
}
