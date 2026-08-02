package com.app.event.auth;

import com.app.model.PasswordResetToken;

public record ResetPasswordTokenConfirmEvent(
    PasswordResetToken token
){}
