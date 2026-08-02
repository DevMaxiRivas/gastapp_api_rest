package com.app.dto.v1.auth;

import jakarta.validation.constraints.Email;

public record ResetPasswordRequest (
        @Email
        String email
){}
