package com.github.konstantin.suspitsyn.ecommercebackend.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class OldNewPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
