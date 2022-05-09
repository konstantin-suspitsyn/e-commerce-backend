package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token;

import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    public static final int TOKEN_EXPIRE_MINUTES = 360;
    public static final String NO_TOKEN_ERROR = "Нет токенов";
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public String saveToken(User user) {

        boolean tokenExists = true;
        String token = "";

        while (tokenExists == true) {
            // Check if token already exists
            token = UUID.randomUUID().toString();
            tokenExists = confirmationTokenRepository.findByToken(token).isPresent();
        }


        ConfirmationToken confirmationToken = new ConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(TOKEN_EXPIRE_MINUTES),
          user
        );

        confirmationTokenRepository.save(confirmationToken);

        return token;
    }

    public ConfirmationToken confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException(NO_TOKEN_ERROR));
        if (LocalDateTime.now().isBefore(confirmationToken.getExpiresAt())) {
            confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
        }
        return confirmationToken;
    }

}
