package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRole;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserService;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token.ConfirmationToken;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class LoginRegistrationService {

    public static final String NEED_ACTIVATE_ACCOUNT = "Нужно активировать аккаунт";
    public static final String SUCCESS_LOGIN = "Пользователь успешно вошел";
    public static final String WRONG_PASSWORD = "Пароль неверный";
    public static final String USER_NAME = "userName";
    public static final String YOU_ARE_OUT = "%s вышел или вышла";
    private final String NON_VALID_EMAIL = "Ваш email не прошел проверку";
    private final String TOKEN_CONFIRMED = "Токен пользователя %s подтвержден";
    public static final String APPLICATION_JSON = "application/json";

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @SneakyThrows
    public void register(RegisterRequest registerRequest, HttpServletResponse response) {
        boolean isValidEmail = emailValidator.emailMatcher(registerRequest.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException(NON_VALID_EMAIL);
        }

        User user = new User(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                UserRole.USER,
                false,
                false
        );

        String token = userService.signUpUser(user);

        Map<String, String> jsonToken = new HashMap<>();
        jsonToken.put("confirmationToken", token);
        response.setContentType(APPLICATION_JSON);
        new ObjectMapper().writeValue(response.getOutputStream(), jsonToken);

        // TODO: send email

    }

    public String confirmUser(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.confirmToken(token);
        User user = confirmationToken.getUser();
        userService.enableUser(user.getId());
        return String.format(TOKEN_CONFIRMED, user.getEmail());
    }

}
