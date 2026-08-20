package com.app.exception.resource;

import com.app.exception.BaseException;
import com.app.exception.codes._4xx.NotFound404Exception;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundCustomException extends NotFound404Exception {
    public ResourceNotFoundCustomException(
            String message,
            String pointer
    ) {
        super(message,pointer);
    }
}