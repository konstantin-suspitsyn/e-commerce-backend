package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.konstantin.suspitsyn.ecommercebackend.configuration.CustomAlgorithm;
import com.github.konstantin.suspitsyn.ecommercebackend.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RequestMapping(path = "registration")
@RestController
@AllArgsConstructor
public class LoginRegistrationController {

    private final LoginRegistrationService loginRegistrationService;


    // New user registration
    @PostMapping(path = "/signup")
    public void register(@RequestBody RegisterRequest registerRequest, HttpServletResponse httpServletResponse) throws IOException {
        loginRegistrationService.register(registerRequest, httpServletResponse);
    }

    // Token confirmation
    @GetMapping(path = "/user-confirm")
    public String confirmUser(@RequestParam String token) {
        return loginRegistrationService.confirmUser(token);
    }

    @GetMapping(path = "/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        loginRegistrationService.refreshToken(request, httpServletResponse);
    }
}
