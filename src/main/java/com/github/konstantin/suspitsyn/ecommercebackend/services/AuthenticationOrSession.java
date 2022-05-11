package com.github.konstantin.suspitsyn.ecommercebackend.services;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationOrSession {

    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

}
