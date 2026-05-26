package com.app.dto.auth;

public record AuthResponse(String token, String email, String name) {}