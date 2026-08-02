package com.app.exception.codes._4xx;

import com.app.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BadRequest400Exception extends BaseException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    public BadRequest400Exception(
            String message,
            String title,
            String pointer
    ) {
        super(
                message,
                STATUS.name(),
                STATUS,
                title,
                pointer
        );
    }
}
