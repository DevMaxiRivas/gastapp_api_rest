package com.app.exception.body;

import com.app.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;

public class HttpMediaTypeNotSupportedCustomException extends BaseException {
    public HttpMediaTypeNotSupportedCustomException(HttpMediaTypeNotSupportedException ex) {
        super(
                "Supported types: " + ex.getSupportedMediaTypes(),
                "UNSUPPORTED_MEDIA_TYPE",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Unsupported Media Type",
                "body"
        );
    }
}
