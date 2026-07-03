package com.app.exception.codes._5xx;

import com.app.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InternalServerError500Exception extends BaseException {
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    public InternalServerError500Exception(
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
