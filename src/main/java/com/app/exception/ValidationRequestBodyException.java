package com.app.exception;

import lombok.Getter;

@Getter
public class ValidationRequestBodyException extends RuntimeException {
    private final String pointer;

    public ValidationRequestBodyException(String message, String pointer) {
        super(message);
        this.pointer = pointer;
    }
}