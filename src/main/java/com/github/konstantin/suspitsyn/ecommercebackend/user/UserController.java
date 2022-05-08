package com.github.konstantin.suspitsyn.ecommercebackend.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "users")
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public Page<User> viewAllUsers() {
        return userService.viewAllUsers();
    }

}
