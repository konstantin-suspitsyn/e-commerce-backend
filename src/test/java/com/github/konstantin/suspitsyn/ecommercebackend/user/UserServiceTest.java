package com.github.konstantin.suspitsyn.ecommercebackend.user;

import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.filter.UserFromTokenService;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token.ConfirmationTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DisplayName("User Service Test")
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Mock
    private UserFromTokenService userFromTokenService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService userService;
    private AutoCloseable autoCloseable;

    private static final String FIRST_NAME = "Имя";
    private static final String LAST_NAME = "Фамилия";
    private static final String EMAIL = "mail@mail.ru";
    private static final String PASSWORD = "password";
    private static final boolean LOCKED = false;
    private static final boolean ENABLED = false;

    private User saveUser() {
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
        return user;
    }

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserService(
                userFromTokenService,
                userRepository,
                confirmationTokenService,
                bCryptPasswordEncoder);
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void shouldLoadUserByUsername() {

        User user = this.saveUser();
        UserDetails userGet = userService.loadUserByUsername(EMAIL);
        assertThat(userGet.getUsername()).isEqualTo(EMAIL);

        userRepository.deleteAll();

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(EMAIL));
    }

    @Test
    void findByEmail() {
        User user = this.saveUser();
        UserDetails userGet = userService.findByEmail(EMAIL);
        assertThat(userGet.getUsername()).isEqualTo(EMAIL);

        userRepository.deleteAll();

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(EMAIL));
    }

}