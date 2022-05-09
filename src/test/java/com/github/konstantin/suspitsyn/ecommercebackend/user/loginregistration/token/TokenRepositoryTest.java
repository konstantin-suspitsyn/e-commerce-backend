package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token;

import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRepository;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("Token repository test")
public class TokenRepositoryTest {

    public static final String NEW_TOKEN = "NEW_TOKEN";
    @Autowired
    UserRepository userRepository;

    ConfirmationToken confirmationToken;

    private static final String FIRST_NAME = "Имя";
    private static final String LAST_NAME = "Фамилия";
    private static final String EMAIL = "mail@mail.ru";
    private static final String PASSWORD = "password";
    private static final boolean LOCKED = false;
    private static final boolean ENABLED = false;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @BeforeEach
    void setUp() {

        User user = new User(
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                PASSWORD,
                UserRole.USER,
                LOCKED,
                ENABLED
        );

        userRepository.save(user);

        confirmationToken = new ConfirmationToken(
                NEW_TOKEN,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60),
                user
        );
        confirmationTokenRepository.save(confirmationToken);
    }

    @AfterEach
    void cleanUp() {
        confirmationTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindByToken() {
        confirmationToken = confirmationTokenRepository.findByToken(NEW_TOKEN)
                .orElseThrow(() -> new IllegalStateException("FU"));

        assertThat(confirmationToken.getToken()).isEqualTo(NEW_TOKEN);
    }

    @Test
    void shouldUpdateConfirmedAt () {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        confirmationTokenRepository.updateConfirmedAt(NEW_TOKEN, localDateTime);

        confirmationToken = confirmationTokenRepository.findByToken(NEW_TOKEN)
                .orElseThrow(() -> new IllegalStateException("FU"));

        assertThat(confirmationToken.getConfirmedAt()).isEqualTo(localDateTime);
    }


}
