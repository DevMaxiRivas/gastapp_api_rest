package com.app.exception.header;

import com.app.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;

@Getter
public class MissingHeaderCustomException extends BaseException {
    public MissingHeaderCustomException(
            MissingRequestHeaderException ex
    ){
        super(
                getDetailFromException(ex),
                "BAD_REQUEST",
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                "headers"
        );
    }

    private static String getDetailFromException(MissingRequestHeaderException ex){
        return ex.getHeaderName() + ": is required";
    }
}
