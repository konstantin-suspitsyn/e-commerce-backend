package com.github.konstantin.suspitsyn.ecommercebackend.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping(path = "users")
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public Page<User> viewAllUsers() {
        return userService.viewAllUsers();
    }

    @GetMapping("/self")
    public void viewSelfInfo(HttpServletRequest request,  HttpServletResponse response) throws IOException {
        userService.viewSelfInfo(request, response);
    }

    @PostMapping("/self/change-password")
    public void changeKnownPassword(@RequestBody OldNewPasswordRequest oldNewPasswordRequest, HttpServletRequest request, HttpServletResponse response) {
        userService.changeKnownPassword(oldNewPasswordRequest, request, response);
    }

    @GetMapping("/make-admin")
    public void makeUserAdmin(@RequestParam String username, HttpServletResponse response) {
        userService.makeUserAdmin(username, response);
    }

}
