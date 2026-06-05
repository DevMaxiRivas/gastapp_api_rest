package com.app.exception.storage;

import com.app.exception.BaseException;
import com.app.exception.LoggeableException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Getter
public class DeleteFileCustomException extends BaseException implements LoggeableException {
    private IOException ex;
    public DeleteFileCustomException(
            IOException ex
    ){
        super(
                "The file could not be deleted.",
                "SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "request"
        );
    }

    public void printLogs(){
        System.err.println(ex.getMessage());
    }
}
