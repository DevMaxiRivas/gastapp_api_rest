package com.app.config.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public AuthEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        Throwable cause = authException.getCause();

        int status;
        String errorCode;
        String message;

        if (cause instanceof ExpiredJwtException) {
            status = HttpStatus.UNAUTHORIZED.value();
            errorCode = "TOKEN_EXPIRED";
            message = "JWT token has expired";
        } else if (cause instanceof MalformedJwtException
                || cause instanceof SignatureException) {
            status = HttpStatus.UNAUTHORIZED.value();
            errorCode = "TOKEN_INVALID";
            message = "JWT token is invalid";
        } else {
            status = HttpStatus.UNAUTHORIZED.value();
            errorCode = "UNAUTHORIZED";
            message = "Authentication is required";
        }

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = Map.of(
                "success", false,
                "statusCode", status,
                "errors", List.of(Map.of(
                        "status", errorCode,
                        "code", status,
                        "title", "Authentication error",
                        "detail", message,
                        "source", Map.of("pointer", request.getRequestURI())
                ))
        );

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}