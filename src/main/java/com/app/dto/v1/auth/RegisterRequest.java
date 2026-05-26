package com.app.dto.auth;

public record RegisterRequest(String name, String email, String password, String currency) {}