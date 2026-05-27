package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.auth.AuthResponse;
import com.app.dto.v1.auth.LoginRequest;
import com.app.dto.v1.auth.RegisterRequest;
import com.app.dto.v1.auth.AccessTokenResponse;
import com.app.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final int EXPIRATION_DAYS = 7;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);

        ResponseCookie cookie = ResponseCookie.from("refresh-token", authResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/auth")
                .maxAge(Duration.ofDays(EXPIRATION_DAYS))
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(
                        ApiResponse.created(
                                authResponse.token()
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse =authService.login(request);

        ResponseCookie cookie = ResponseCookie.from("refresh-token", authResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .maxAge(Duration.ofDays(EXPIRATION_DAYS))
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(
                        ApiResponse.success(
                                authResponse.token()
                        )
                );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refresh-token") String refreshToken) {
        authService.logout(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refresh-token", null)
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> refresh(@CookieValue(value = "refresh-token") String refreshToken) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                authService.refresh(refreshToken)
                        )
                );
    }
}