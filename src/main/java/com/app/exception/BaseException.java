package com.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {
    private final String status;
    private final HttpStatus httpStatus;
    private final String title;
    private final String pointer;

    public BaseException(String message, String status, HttpStatus httpStatus, String title, String pointer) {
        super(message);
        this.status = status;
        this.httpStatus = httpStatus;
        this.title = title;
        this.pointer = pointer;
    }
}