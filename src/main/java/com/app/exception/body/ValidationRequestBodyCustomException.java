package com.app.exception.body;

import com.app.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationRequestBodyCustomException extends BaseException {
    public ValidationRequestBodyCustomException(
            String message,
            String pointer
    ) {
        super(
                message,
                "VALIDATION_ERROR",
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Invalid request",
                pointer
        );
    }
}