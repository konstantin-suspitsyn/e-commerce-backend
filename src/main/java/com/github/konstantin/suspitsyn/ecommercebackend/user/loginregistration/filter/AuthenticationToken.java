package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.konstantin.suspitsyn.ecommercebackend.configuration.CustomAlgorithm;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.User;

public class AuthenticationToken {

    Algorithm algorithm;

    public AuthenticationToken() {
        this.algorithm = CustomAlgorithm.encryptionAlgorithm();
    }

    public String authenticationToken(User user, String resultUrl, int expireMinutes) {
    return  JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + expireMinutes * 60 * 1000))
            .withIssuer(resultUrl)
            .withClaim(
                    "roles",
                    user.getAuthorities()
                            .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .sign(algorithm);
    }

    public String authenticationToken(com.github.konstantin.suspitsyn.ecommercebackend.user.User user, String resultUrl, int expireMinutes) {
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(user.getUserRole());
        return  JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expireMinutes * 60 * 1000))
                .withIssuer(resultUrl)
                .withClaim(
                        "roles",
                        userRoles)
                .sign(algorithm);
    }


}
