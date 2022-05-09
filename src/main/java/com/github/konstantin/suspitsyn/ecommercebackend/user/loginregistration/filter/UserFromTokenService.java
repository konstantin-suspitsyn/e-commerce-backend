package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.filter;

import com.github.konstantin.suspitsyn.ecommercebackend.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static java.util.Arrays.stream;

@Service
@AllArgsConstructor
public class UserFromTokenService {


    public String getUserNameFromJWT(HttpServletRequest request) {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
