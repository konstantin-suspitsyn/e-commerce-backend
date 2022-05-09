package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.konstantin.suspitsyn.ecommercebackend.configuration.CustomAlgorithm;
import com.github.konstantin.suspitsyn.ecommercebackend.services.StaticServices;
import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRole;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserService;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.filter.AuthenticationToken;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token.ConfirmationToken;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@AllArgsConstructor
public class LoginRegistrationService {

    private final String NON_VALID_EMAIL = "Ваш email не прошел проверку";
    private final String TOKEN_CONFIRMED = "Токен пользователя %s подтвержден";
    // Bearer authentication (also called token authentication)
    public static final String BEARER_ = "Bearer ";

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;


    public void register(RegisterRequest registerRequest, HttpServletResponse response) throws IOException {
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
        StaticServices.mapResponse(response, jsonToken);

        // TODO: send email

    }

    public String confirmUser(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.confirmToken(token);
        User user = confirmationToken.getUser();
        userService.enableUser(user.getId());
        return String.format(TOKEN_CONFIRMED, user.getEmail());
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_)) {
            try {
                // RefreshToken Check
                String refreshToken = authorizationHeader.substring(BEARER_.length());
                Algorithm algorithm = CustomAlgorithm.encryptionAlgorithm();
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                org.springframework.security.core.userdetails.User user =
                        (org.springframework.security.core.userdetails.User)
                                userService.loadUserByUsername(username);
                // Creating new accessToken
                AuthenticationToken authenticationToken = new AuthenticationToken();
                String accessToken = authenticationToken.authenticationToken(user, request.getRequestURL().toString(), 24*60);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                StaticServices.mapResponse(httpServletResponse, tokens);
            } catch (Exception exception) {
                httpServletResponse.setHeader("error", exception.getMessage());
                httpServletResponse.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                StaticServices.mapResponse(httpServletResponse, error);;

            }
        } else {
            throw new RuntimeException("Refresh token отсутствет");
        }
    }


}
