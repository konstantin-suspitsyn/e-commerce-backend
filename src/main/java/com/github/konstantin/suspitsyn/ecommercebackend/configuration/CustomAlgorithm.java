package com.github.konstantin.suspitsyn.ecommercebackend.configuration;

import com.auth0.jwt.algorithms.Algorithm;

public class CustomAlgorithm {

    public static Algorithm encryptionAlgorithm() {
        // Put SECRET_ENCRYPTION into properties file
        return Algorithm.HMAC256("SECRET_ENCRYPTION".getBytes());
    }


}
