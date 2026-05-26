package com.app.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final String pointer;

    public ValidationException(String message, String pointer) {
        super(message);
        this.pointer = pointer;
    }
}