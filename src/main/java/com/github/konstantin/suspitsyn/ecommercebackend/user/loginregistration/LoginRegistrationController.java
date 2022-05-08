package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping(path = "registration")
@RestController
@AllArgsConstructor
public class LoginRegistrationController {

    private final LoginRegistrationService loginRegistrationService;

    // New user registration
    @PostMapping(path = "/signup")
    public void register(@RequestBody RegisterRequest registerRequest, HttpServletResponse httpServletResponse) {
        System.out.println("signup");
        loginRegistrationService.register(registerRequest, httpServletResponse);
    }

    // Token confirmation
    @GetMapping(path = "/user-confirm")
    public String confirmUser(@RequestParam String token) {
        return loginRegistrationService.confirmUser(token);
    }

    @GetMapping(path = "/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse) {

    }

}
