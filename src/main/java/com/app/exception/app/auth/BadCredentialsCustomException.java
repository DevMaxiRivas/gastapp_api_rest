package com.app.exception.app.auth;

import com.app.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadCredentialsCustomException extends BaseException {
    public BadCredentialsCustomException(){
        super(
                "Credentials Incorrect",
                "UNAUTHORIZED",
                HttpStatus.UNAUTHORIZED,
                "Authentication Failed",
                "body"
        );
    }
}