package com.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericErrorException extends BaseException implements LoggeableException{
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
        System.err.println("GenericErrorException");
        if(this.ex != null){
            System.out.println(this.ex.getClass().getSimpleName());
            System.out.println(this.ex.getMessage());
        }
    }
}
