package com.github.konstantin.suspitsyn.ecommercebackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE User u " +
            "SET u.enabled = true " +
            "WHERE u.id = ?1")
    void setEnable(Long id);

}
