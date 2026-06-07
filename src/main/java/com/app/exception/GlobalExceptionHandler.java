package com.app.exception;

import com.app.dto.v1.error.ErrorResponse;

import com.app.exception.app.auth.AuthorizationDeniedCustomException;
import com.app.exception.app.auth.BadCredentialsCustomException;
import com.app.exception.app.auth.jwt.InvalidJwtCustomException;
import com.app.exception.body.HttpMediaTypeNotSupportedCustomException;
import com.app.exception.body.NotReadableBodyCustomException;
import com.app.exception.cookie.MissingCookieCustomException;
import com.app.exception.header.MissingHeaderCustomException;
import com.app.exception.app.auth.jwt.ExpiredJwtCustomException;

import com.app.exception.method.HttpRequestMethodNotSupportedCustomException;
import com.app.exception.resource.ResourceNotFoundCustomException;
import com.app.exception.validation.MethodArgumentNotValidCustomException;
import com.app.exception.validation.MissingServletRequestPartCustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return (new MethodArgumentNotValidCustomException(ex,request.getRequestURL().toString())).buildErrorResponse();
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        return ex.buildErrorResponse(request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadableBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return (new NotReadableBodyCustomException(ex)).buildErrorResponse(request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingRequestHeaderException ex, HttpServletRequest request) {
        return (new MissingHeaderCustomException(ex)).buildErrorResponse(request);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingCookie(MissingRequestCookieException ex, HttpServletRequest request) {
        return (new MissingCookieCustomException(ex)).buildErrorResponse(request);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex, HttpServletRequest request) {
        return (new ExpiredJwtCustomException()).buildErrorResponse(request);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureError(SignatureException ex, HttpServletRequest request) {
        return (new InvalidJwtCustomException()).buildErrorResponse(request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return (new BadCredentialsCustomException()).buildErrorResponse(request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        return (new ResourceNotFoundCustomException("Resource not found", "url")).buildErrorResponse(request);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return (new HttpMediaTypeNotSupportedCustomException(ex)).buildErrorResponse(request);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequest(MissingServletRequestPartException ex, HttpServletRequest request) {
        return (new MissingServletRequestPartCustomException(ex)).buildErrorResponse(request);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException ex, HttpServletRequest request) {
        AuthorizationDeniedCustomException exception =  new AuthorizationDeniedCustomException(ex);
        exception.printLogs();
        return exception.buildErrorResponse(request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return (new HttpRequestMethodNotSupportedCustomException(ex)).buildErrorResponse(request);
    }

    // Generic Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex, HttpServletRequest request) {
        GenericErrorException error = new GenericErrorException(ex);
        error.printLogs();
        return error.buildErrorResponse(request);
    }
}
