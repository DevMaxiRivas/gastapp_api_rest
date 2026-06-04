package com.app.dto.v1.auth;

import com.app.enums.user.CurrencyType;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotNull()
        @Size(min = 3)
        String name,

        @NotNull()
        @Email()
        String email,

        @NotNull()
        @Size(min = 8)
        String password
) {}