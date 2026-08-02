package com.app.event.auth;

import com.app.model.PasswordResetToken;

public record ResetPasswordTokenCreateEvent (
    PasswordResetToken token
){}
