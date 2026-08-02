package com.app.dto.v1.auth;

import com.app.validation.password.ValidPassword;

public record ConfirmResetPasswordRequest(
        String token,

        @ValidPassword
        String newPassword
){}
