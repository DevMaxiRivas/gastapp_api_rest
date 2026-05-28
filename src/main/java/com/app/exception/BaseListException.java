package com.app.exception;

import com.app.dto.v1.error.ErrorDetail;
import com.app.dto.v1.error.ErrorResponse;
import com.app.dto.v1.error.Links;
import com.app.dto.v1.error.Source;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseListException extends Exception {
    protected abstract List<ErrorDetail> getListErrorDetail();

    public ResponseEntity<ErrorResponse> buildErrorResponse() {
        ErrorResponse response = ErrorResponse.builder()
                .status("error")
                .errors(getListErrorDetail())
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
