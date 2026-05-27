package com.app.exception;

import com.app.dto.v1.error.ErrorDetail;
import com.app.dto.v1.error.ErrorResponse;
import com.app.dto.v1.error.Links;
import com.app.dto.v1.error.Source;

import com.app.exception.app.auth.BadCredentialsCustomException;
import com.app.exception.app.auth.jwt.InvalidJwtCustomException;
import com.app.exception.body.NotReadableBodyCustomException;
import com.app.exception.cookie.MissingCookieCustomException;
import com.app.exception.header.MissingHeaderCustomException;
import com.app.exception.app.auth.jwt.ExpiredJwtCustomException;

import com.app.exception.resource.ResourceNotFoundCustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.environment}")
    private String environment;

    // 1. Validation Error Manager (ej. @Valid DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorDetail> errorDetails = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorDetail
                        .builder()
                        .status("VALIDATION_ERROR")
                        .code(HttpStatus.UNPROCESSABLE_CONTENT.value())
                        .title("Invalid request")
                        .detail(error.getField() + ": " + error.getDefaultMessage())
                        .source(new Source("body/" + error.getField()))
                        .links(new Links(request.getRequestURL().toString()))
                        .build()
                )
                .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
                .status("error")
                .errors(errorDetails)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String status,
            HttpStatus httpStatus,
            String title,
            String detail,
            String pointer,
            HttpServletRequest request) {

        ErrorDetail errorDetail = ErrorDetail.builder()
                .status(status)
                .code(httpStatus.value())
                .title(title)
                .detail(detail)
                .source(new Source(pointer))
                .links(new Links(request.getRequestURL().toString()))
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder().status("error").errors(List.of(errorDetail)).build(),
                httpStatus
        );
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getStatus(),ex.getHttpStatus(),ex.getTitle(),ex.getMessage(),ex.getPointer(),request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadableBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return handleBaseException(new NotReadableBodyCustomException(ex), request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingRequestHeaderException ex, HttpServletRequest request) {
        return handleBaseException(new MissingHeaderCustomException(ex), request);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingCookie(MissingRequestCookieException ex, HttpServletRequest request) {
        return handleBaseException(new MissingCookieCustomException(ex), request);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex, HttpServletRequest request) {
        return handleBaseException(new ExpiredJwtCustomException(), request);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureError(SignatureException ex, HttpServletRequest request) {
        return handleBaseException(new InvalidJwtCustomException(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return handleBaseException(new BadCredentialsCustomException(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        return handleBaseException(new ResourceNotFoundCustomException("Resource not found", "url"), request);
    }

    // Generic Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex, HttpServletRequest request) {
        System.out.println(ex.getClass().getSimpleName());
        System.out.println(ex.getMessage());
        ErrorDetail error = ErrorDetail.builder()
                .status("INTERNAL_SERVER_ERROR")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title("Server error")
                .detail(Objects.equals(this.environment, "dev") ? ex.getMessage() : "Internal Server Error")
                .source(new Source("unknown"))
                .links(new Links(request.getRequestURL().toString()))
                .build();

        return new ResponseEntity<>(
                ErrorResponse
                        .builder()
                        .status("error")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
