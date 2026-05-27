package com.app.dto.v1.auth;

public record AuthResponse(
        AccessTokenResponse token,
        String refreshToken
) {}