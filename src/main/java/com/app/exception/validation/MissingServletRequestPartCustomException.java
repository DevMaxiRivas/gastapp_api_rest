package com.app.exception.validation;

import com.app.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

public class MissingServletRequestPartCustomException extends BaseException {
    public MissingServletRequestPartCustomException(MissingServletRequestPartException ex) {
        super(
                ex.getRequestPartName() + " is required",
                "UNPROCESSABLE_CONTENT",
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Parameter Missing",
                "body"
        );
    }
}
