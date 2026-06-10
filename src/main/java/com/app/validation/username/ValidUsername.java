package com.app.validation.username;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {

    String message() default
            "Must be 3-15 characters long and can only contain letters, numbers, dots, underscores, or hyphens";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}