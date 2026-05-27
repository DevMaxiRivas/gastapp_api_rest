package com.app.exception.resource;

import com.app.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundCustomException extends BaseException {
    public ResourceNotFoundCustomException(
            String message,
            String pointer
    ) {
        super(
                message,
                "NOT_FOUND",
                HttpStatus.NOT_FOUND,
                "Resource not found",
                pointer
        );
    }
}