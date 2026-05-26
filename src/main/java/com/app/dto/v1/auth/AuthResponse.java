package com.app.dto.v1.auth;

public record AuthResponse(String token, String email, String name) {}