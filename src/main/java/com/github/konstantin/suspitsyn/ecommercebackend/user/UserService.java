package com.github.konstantin.suspitsyn.ecommercebackend.user;

import com.github.konstantin.suspitsyn.ecommercebackend.services.StaticServices;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.filter.UserFromTokenService;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserFromTokenService userFromTokenService;

    public static final String USER_ID_NOT_FOUND = "Пользователь не найден";
    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String USER_NOT_FOUND = "Пользователь с почтой %s не найден";
    private final String EMAIL_TAKEN = "Пользователь с такой почтой уже существует";


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails tempUser = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));

        return new org.springframework.security.core.userdetails
                .User(tempUser.getUsername(), tempUser.getPassword(), tempUser.getAuthorities());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email))
        );
    }

    public String signUpUser(User user) {

        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (userExists) {
            throw new IllegalStateException(EMAIL_TAKEN);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        String token = confirmationTokenService.saveToken(user);

        return token;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(USER_ID_NOT_FOUND));
    }

    public void enableUser(Long id) {
        userRepository.setEnable(id);
    }

    public Page<User> viewAllUsers() {
        return new PageImpl<>(userRepository.findAll());
    }

    public void viewSelfInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = this.userFromToken(request);
        Map<String, String> jsonUser = new HashMap<>();
        jsonUser.put("firstName", user.getFirstName());
        jsonUser.put("lastName", user.getLastName());
        jsonUser.put("email", user.getEmail());
        jsonUser.put("userRole", String.valueOf(user.getUserRole()));
        StaticServices.mapResponse(response, jsonUser);
    }

    public void changeKnownPassword(OldNewPasswordRequest oldNewPasswordRequest,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        User user = this.userFromToken(request);
        String oldPassword = oldNewPasswordRequest.getOldPassword();
        String newPassword = bCryptPasswordEncoder.encode(oldNewPasswordRequest.getNewPassword());

        if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            userRepository.updatePassword(user.getId(), newPassword);
        }

        response.setStatus(200);

    }

    private User userFromToken(HttpServletRequest request) {
        return this.findByEmail(userFromTokenService.getUserNameFromJWT(request));
    }

    public void makeUserAdmin(String username, HttpServletResponse response) {
        System.out.println(username);
        System.out.println(UserRole.ADMIN.name());
        userRepository.makeUserAdmin(username, UserRole.ADMIN);
        response.setStatus(200);
    }
}
