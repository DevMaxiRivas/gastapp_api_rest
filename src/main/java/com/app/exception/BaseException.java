package com.app.exception;

import com.app.dto.v1.error.ErrorDetail;
import com.app.dto.v1.error.ErrorResponse;
import com.app.dto.v1.error.Links;
import com.app.dto.v1.error.Source;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class BaseException extends RuntimeException {
    private final String status;
    private final HttpStatus httpStatus;
    private final String title;
    private final String pointer;

    public BaseException(String message, String status, HttpStatus httpStatus, String title, String pointer) {
        super(message);
        this.status = status;
        this.httpStatus = httpStatus;
        this.title = title;
        this.pointer = pointer;
    }

    public ResponseEntity<ErrorResponse> buildErrorResponse(HttpServletRequest request) {

        ErrorDetail errorDetail = ErrorDetail.builder()
                .status(this.status)
                .code(this.httpStatus.value())
                .title(this.title)
                .detail(this.getMessage())
                .source(new Source(this.pointer))
                .links(new Links(request.getRequestURI()))
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder().errors(List.of(errorDetail)).build(),
                this.httpStatus
        );
    }
}