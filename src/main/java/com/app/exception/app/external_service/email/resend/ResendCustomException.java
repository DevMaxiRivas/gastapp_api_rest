package com.app.exception.app.external_service.email.resend;

import com.app.exception.LoggeableException;
import com.app.exception.codes._5xx.GatewayTimeout504Exception;
import com.resend.core.exception.ResendException;

public class ResendCustomException extends GatewayTimeout504Exception implements LoggeableException {
    private final ResendException e;

    public ResendCustomException(ResendException e) {
        super(
            "Occurs error at send email",
            "Fail send email",
            ""
        );
        this.e = e;
    }

    @Override
    public void printLogs() {
        System.err.println(e.getMessage());
    }
}
