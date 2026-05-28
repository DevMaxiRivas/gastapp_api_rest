package com.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericErrorException extends BaseException{
    protected Exception ex;

    public GenericErrorException(
            Exception ex
    ){
        super(
                "Internal Server Error",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Server error",
                "unknown"
        );
    }

    public void printLogs(){
        System.out.println(this.ex.getClass().getName());
        System.out.println(this.ex.getMessage());
    }
}
