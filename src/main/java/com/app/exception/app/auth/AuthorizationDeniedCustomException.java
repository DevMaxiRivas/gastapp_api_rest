package com.app.exception.app.auth;

import com.app.exception.BaseException;
import com.app.exception.LoggeableException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;

public class AuthorizationDeniedCustomException extends BaseException implements LoggeableException {
    private final AuthorizationDeniedException ex;
    public AuthorizationDeniedCustomException(AuthorizationDeniedException ex){
        super(
                "Access Denied",
                "UNAUTHORIZED",
                HttpStatus.UNAUTHORIZED,
                "Authorization Failed",
                "token"
        );
        this.ex = ex;
    }


    @Override
    public void printLogs() {
        System.err.println(ex.getMessage());
    }
}
