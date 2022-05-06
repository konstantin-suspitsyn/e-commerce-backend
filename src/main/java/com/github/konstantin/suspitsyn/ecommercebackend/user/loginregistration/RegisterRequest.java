package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
