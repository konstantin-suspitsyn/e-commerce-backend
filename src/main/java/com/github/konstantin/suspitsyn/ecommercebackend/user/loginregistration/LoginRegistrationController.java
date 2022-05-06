package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(path = "registration")
@RestController
@AllArgsConstructor
public class LoginRegistrationController {

    private final LoginRegistrationService loginRegistrationService;

    // New user registration
    @PostMapping(path = "/signup")
    public String register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("signup");
        return loginRegistrationService.register(registerRequest);
    }

    // Token confirmation
    @GetMapping(path = "/user-confirm")
    public String confirmUser(@RequestParam String token) {
        return loginRegistrationService.confirmUser(token);
    }

    // Login to system
    // Enables session and sets attribute userName = User.email
    @PostMapping(path = "/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        return loginRegistrationService.login(loginRequest, httpServletRequest);
    }

    // Removes attribute userName from session
    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        return loginRegistrationService.logout(httpServletRequest);
    }

}
