package com.github.konstantin.suspitsyn.ecommercebackend.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("User repository test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final String FIRST_NAME = "Имя";
    private static final String LAST_NAME = "Фамилия";
    private static final String EMAIL = "mail@mail.ru";
    private static final String PASSWORD = "password";
    private static final boolean LOCKED = false;
    private static final boolean ENABLED = false;

    @BeforeEach
    void setUp() {
        // given
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
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldFindByEmail() {
        User userGet = userRepository.findByEmail(EMAIL)
                .orElseThrow(() -> new IllegalStateException("FU"));

        assertThat(userGet.getFirstName()).isEqualTo(FIRST_NAME);
    }

    @Test
    void shouldFindById() {
        // given
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

        Long id = user.getId();

        User userGet = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("FU"));

        assertThat(userGet.getFirstName()).isEqualTo(FIRST_NAME);

    }

    @Test
    void shouldSetEnableToUser() {
        User userGet = userRepository.findByEmail(EMAIL)
                .orElseThrow(() -> new IllegalStateException("FU"));

        assertThat(userGet.getEnabled()).isEqualTo(ENABLED);

        userRepository.setEnable(userGet.getId());

        User userGetUpd = userRepository.findByEmail(EMAIL)
                .orElseThrow(() -> new IllegalStateException("FU"));

        assertThat(userGetUpd.getEnabled()).isEqualTo(!ENABLED);
    }

}
