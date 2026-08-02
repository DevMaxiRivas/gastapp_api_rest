package com.app.exception.query_params;

import com.app.exception.codes._4xx.BadRequest400Exception;
import org.springframework.web.bind.MissingServletRequestParameterException;

public class MissingServletRequestParameterCustomException  extends BadRequest400Exception {
    protected  MissingServletRequestParameterException e;

    public MissingServletRequestParameterCustomException(
            MissingServletRequestParameterException e
    ){
        super(
                "An error occurred while processing the request",
                e.getParameterName() + " was not sent in the request",
                "query_params"
        );
        this.e = e;
    }
}
