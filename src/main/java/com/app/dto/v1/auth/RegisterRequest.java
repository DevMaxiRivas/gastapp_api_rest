package com.app.dto.v1.auth;

import com.app.enums.user.CurrencyType;
import com.app.validation.password.ValidPassword;
import com.app.validation.username.ValidUsername;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotNull()
        @ValidUsername
        String username,

        @NotNull()
        @Email()
        String email,

        @NotNull()
        @ValidPassword
        String password
) {}