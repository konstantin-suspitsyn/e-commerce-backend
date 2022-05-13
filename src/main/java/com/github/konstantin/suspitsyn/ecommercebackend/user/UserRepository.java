package com.github.konstantin.suspitsyn.ecommercebackend.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u " +
            "SET u.enabled = true " +
            "WHERE u.id = ?1")
    void setEnable(Long id);

    Page<User> findByEmailContaining(@RequestParam("email") String email, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u " +
            "SET u.firstName = ?1 " +
            ",u.lastName = ?2 " +
            ",u.password = ?3 " +
            ",u.userRole = ?4 " +
            "WHERE u.id = ?5")
    void updateUserData(String firstName, String lastName, String password,
                        String userRole, Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u " +
            "SET u.password = ?2 " +
            "WHERE u.id = ?1")
    void updatePassword(Long id, String password);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u " +
            "SET u.userRole = ?2 " +
            "WHERE u.email = ?1")
    void makeUserAdmin(String username, UserRole role);

}
