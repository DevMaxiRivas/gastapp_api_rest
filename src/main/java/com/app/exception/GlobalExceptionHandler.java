package com.app.exception;

import com.app.dto.v1.error.ErrorDetail;
import com.app.dto.v1.error.ErrorResponse;
import com.app.dto.v1.error.Links;
import com.app.dto.v1.error.Source;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.environment}")
    private String environment;

    // 1. Validation Error Manager (ej. @Valid DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
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

    @ExceptionHandler(ValidationRequestBodyException.class)
    public ResponseEntity<ErrorResponse> handleManualValidation(ValidationRequestBodyException ex, HttpServletRequest request) {
        ErrorDetail error = ErrorDetail.builder()
                .status("VALIDATION_ERROR")
                .code(HttpStatus.UNPROCESSABLE_CONTENT.value())
                .title("Invalid request")
                .detail(ex.getMessage())
                .source(new Source(ex.getPointer()))
                .links(new Links(request.getRequestURL().toString()))
                .build();

        ErrorResponse response = ErrorResponse.builder()
                .status("error")
                .errors(List.of(error))
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_CONTENT);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorDetail error = ErrorDetail.builder()
                .status("NOT_FOUND")
                .code(HttpStatus.NOT_FOUND.value())
                .title("Resource not found")
                .detail(ex.getMessage())
                .source(new Source("url_parameter"))
                .links(new Links(request.getRequestURL().toString()))
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status("error")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadableBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String errorMessage = ex.getMessage();
        // Check if the root cause is an invalid enum value
        if (ex.getCause() instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            String invalidValue = ife.getValue().toString();
            String acceptedValues = Arrays.toString(ife.getTargetType().getEnumConstants());

            errorMessage = String.format("Invalid value '%s'. Accepted values are: %s",
                    invalidValue, acceptedValues);
        }

        ErrorDetail error = ErrorDetail.builder()
                .status("BAD_REQUEST")
                .code(HttpStatus.BAD_REQUEST.value())
                .title("Invalid request")
                .detail(errorMessage)
                .source(new Source("body"))
                .links(new Links(request.getRequestURL().toString()))
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status("error")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingRequestHeaderException ex, HttpServletRequest request) {
        ErrorDetail error = ErrorDetail.builder()
                .status("BAD_REQUEST")
                .code(HttpStatus.BAD_REQUEST.value())
                .title("Invalid request")
                .detail(ex.getHeaderName() + ": is required")
                .source(new Source("headers"))
                .links(new Links(request.getRequestURL().toString()))
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status("error")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingCookie(MissingRequestCookieException ex, HttpServletRequest request) {
        ErrorDetail error = ErrorDetail.builder()
                .status("BAD_REQUEST")
                .code(HttpStatus.BAD_REQUEST.value())
                .title("Invalid request")
                .detail(ex.getCookieName() + ": is required")
                .source(new Source("cookies"))
                .links(new Links(request.getRequestURL().toString()))
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status("error")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleMissingCookie(ExpiredJwtException ex, HttpServletRequest request) {
        ErrorDetail error = ErrorDetail.builder()
                .status("UNAUTHORIZED")
                .code(HttpStatus.UNAUTHORIZED.value())
                .title("Authentication token expired")
                .detail("jwt is expired")
                .source(new Source("headers"))
                .links(new Links(request.getRequestURL().toString()))
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status("error")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.BAD_REQUEST
        );
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
