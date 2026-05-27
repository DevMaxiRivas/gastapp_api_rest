package com.app.exception.cookie;

import com.app.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestCookieException;

@Getter
public class MissingCookieCustomException extends BaseException {
    public MissingCookieCustomException(
            MissingRequestCookieException ex
    ){
        super(
                getDetailFromException(ex),
                "BAD_REQUEST",
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                "cookies"
        );
    }

    private static String getDetailFromException(MissingRequestCookieException ex){
        return ex.getCookieName() + ": is required";
    }
}
