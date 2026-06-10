package com.app.validation.username;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UsernameValidator
        implements ConstraintValidator<ValidUsername, String> {

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z0-9]+(?:[._@-]?[A-Za-z0-9]+)*$");

    @Override
    public boolean isValid(
        String username,
        ConstraintValidatorContext context
    ) {

        if (username == null) {
            return false;
        }

        context.disableDefaultConstraintViolation();

        if (username.length() < 3) {
            context.buildConstraintViolationWithTemplate(
                            "Must have at least 3 characters")
                    .addConstraintViolation();
            return false;
        }

        if (username.length() > 15) {
            context.buildConstraintViolationWithTemplate(
                            "Shouldn't be more than 15 characters long")
                    .addConstraintViolation();
            return false;
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            context.buildConstraintViolationWithTemplate(
                            "It must begin and end with a letter or a number. The only special characters allowed are ‘_’, ‘-’, ‘.’, and ‘@’.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}