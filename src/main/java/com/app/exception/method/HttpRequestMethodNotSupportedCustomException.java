package com.app.exception.method;

import com.app.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;

public class HttpRequestMethodNotSupportedCustomException extends BaseException {
    public HttpRequestMethodNotSupportedCustomException(HttpRequestMethodNotSupportedException ex) {
        super(
                "Request method '"+ ex.getMethod() + "' not supported",
                "METHOD_NOT_ALLOWED",
                HttpStatus.METHOD_NOT_ALLOWED,
                "Method Not Allowed",
                "method"
        );
    }
}
