package com.app.exception.app.auth.jwt;

import com.app.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidJwtCustomException extends BaseException {
    public InvalidJwtCustomException(){
        super(
                "JWT invalid",
                "UNAUTHORIZED",
                HttpStatus.UNAUTHORIZED,
                "Authentication token is Invalid",
                "headers"
        );
    }
}
