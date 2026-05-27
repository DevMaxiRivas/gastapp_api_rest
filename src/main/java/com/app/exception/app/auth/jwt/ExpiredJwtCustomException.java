package com.app.exception.app.auth.jwt;

import com.app.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExpiredJwtCustomException extends BaseException {
    public ExpiredJwtCustomException(){
        super(
                "JWT expired",
                "UNAUTHORIZED",
                HttpStatus.UNAUTHORIZED,
                "Authentication token expired",
                "headers"
        );
    }
}
