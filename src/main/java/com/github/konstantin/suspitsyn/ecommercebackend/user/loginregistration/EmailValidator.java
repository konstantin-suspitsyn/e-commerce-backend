package com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator {
    private final String patternMailRegexWithAt = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public boolean emailMatcher(String email) {
        if (email.matches(patternMailRegexWithAt)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        EmailValidator emailValidator = new EmailValidator();
        System.out.println(emailValidator.emailMatcher("ks@m.ru"));
        System.out.println(emailValidator.emailMatcher("k.s m.ru"));
        System.out.println(emailValidator.emailMatcher("k .s@m.ru"));
    }

}
