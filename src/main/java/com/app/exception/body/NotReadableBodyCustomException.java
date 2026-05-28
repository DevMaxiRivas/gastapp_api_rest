package com.app.exception.body;

import com.app.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;

@Getter
public class NotReadableBodyCustomException extends BaseException {
    public NotReadableBodyCustomException(
            HttpMessageNotReadableException ex
    ) {
        super(
                extractReadableMessage(ex),
                "BAD_REQUEST",
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                "body"
        );
    }

    private static String getFieldException(InvalidFormatException e) {
        return e.getPath().get(e.getPath().size() - 1).getPropertyName();
    }

    public static String extractReadableMessage(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            return String.format("%s: Invalid value '%s'. Accepted: %s",
                    getFieldException(ife), ife.getValue(), Arrays.toString(ife.getTargetType().getEnumConstants()));
        }
        System.out.println(ex.getClass().getName() + ":" + ex.getMessage());
        return "It was not possible to read the body of the request";
    }
}