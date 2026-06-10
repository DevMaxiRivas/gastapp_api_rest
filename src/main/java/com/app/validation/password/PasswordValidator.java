package com.app.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator
        implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password,
                           ConstraintValidatorContext context) {

        if (password == null) {
            return false;
        }

        context.disableDefaultConstraintViolation();

        if (password.length() < 8) {
            context.buildConstraintViolationWithTemplate(
                            "Must have at least 8 characters")
                    .addConstraintViolation();
            return false;
        }

        if (password.chars().noneMatch(Character::isUpperCase)) {
            context.buildConstraintViolationWithTemplate(
                            "Must contain an uppercase letter")
                    .addConstraintViolation();
            return false;
        }

        if (password.chars().noneMatch(Character::isLowerCase)) {
            context.buildConstraintViolationWithTemplate(
                            "Must contain a lowercase letter")
                    .addConstraintViolation();
            return false;
        }

        if (password.chars().noneMatch(Character::isDigit)) {
            context.buildConstraintViolationWithTemplate(
                            "Must contain a digit")
                    .addConstraintViolation();
            return false;
        }

        if (password.chars().allMatch(Character::isLetterOrDigit)) {
            context.buildConstraintViolationWithTemplate(
                            "Must contain a special character")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}