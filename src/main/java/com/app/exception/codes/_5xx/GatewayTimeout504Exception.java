package com.app.exception.codes._5xx;

import com.app.exception.BaseException;
import org.springframework.http.HttpStatus;

public class GatewayTimeout504Exception extends BaseException {
    private static final HttpStatus STATUS = HttpStatus.GATEWAY_TIMEOUT;
    public GatewayTimeout504Exception(
            String message,
            String title,
            String pointer
    ) {
        super(
                message,
                STATUS.name(),
                STATUS,
                title,
                pointer
        );
    }
}
