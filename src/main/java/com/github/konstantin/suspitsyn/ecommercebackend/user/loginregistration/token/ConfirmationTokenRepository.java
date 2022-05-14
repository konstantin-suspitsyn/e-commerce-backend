package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token;

import org.aspectj.lang.annotation.Before;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE ConfirmationToken ct " +
            "SET ct.confirmedAt = ?2 " +
            "WHERE ct.token = ?1")
    void updateConfirmedAt(String token, LocalDateTime confirmedAt);


}
