package com.app.exception.storage;

import com.app.exception.BaseException;
import com.app.exception.LoggeableException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.io.IOException;

@Getter
public class SaveFileCustomException extends BaseException implements LoggeableException {
    private IOException ex;
    public SaveFileCustomException(
            IOException ex
    ){
        super(
                "The file could not be saved.",
                "SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "body"
        );
    }

    public void printLogs(){
        System.err.println(ex.getMessage());
    }
}
