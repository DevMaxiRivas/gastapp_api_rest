package com.app.exception.codes._4xx;

import com.app.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFound404Exception extends BaseException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String title = "Resource not found";
    public NotFound404Exception(
            String message,
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
