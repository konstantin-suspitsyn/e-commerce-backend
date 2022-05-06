package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration;

import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRole;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserService;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token.ConfirmationToken;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public String register(RegisterRequest registerRequest) {
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

        return token;
    }

    public String confirmUser(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.confirmToken(token);
        User user = confirmationToken.getUser();
        userService.enableUser(user.getId());
        return String.format(TOKEN_CONFIRMED, user.getEmail());
    }

    public String login(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        UserDetails user = userService.loadUserByUsername(loginRequest.getEmail());

        if (!user.isEnabled()) {
            throw new IllegalStateException(NEED_ACTIVATE_ACCOUNT);
        }


        if ((bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword()))) {
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute(USER_NAME, user.getUsername());
            return SUCCESS_LOGIN;
        } else {
            return WRONG_PASSWORD;
        }

    }

    public String logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        String userName = String.valueOf(session.getAttribute(USER_NAME));
        session.removeAttribute(USER_NAME);
        return String.format(YOU_ARE_OUT, userName);
    }
}
