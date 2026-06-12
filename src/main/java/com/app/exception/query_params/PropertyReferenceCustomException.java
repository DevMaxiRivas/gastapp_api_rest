package com.app.exception.query_params;

import com.app.exception.BaseException;
import com.app.exception.LoggeableException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;

public class PropertyReferenceCustomException extends BaseException implements LoggeableException {
    private final PropertyReferenceException ex;

    public PropertyReferenceCustomException( PropertyReferenceException ex) {
        super(
                "Unexpected parameter " + ex.getPropertyName(),
                "BAD_REQUEST",
                HttpStatus.BAD_REQUEST,
                "Unexpected parameter",
                "query_params"
        );
        this.ex = ex;
    }

    @Override
    public void printLogs() {
        System.out.println("PropertyReferenceCustomException");
        System.out.println(this.ex.getMessage());
        System.out.println(this.ex.getType());
    }
}
