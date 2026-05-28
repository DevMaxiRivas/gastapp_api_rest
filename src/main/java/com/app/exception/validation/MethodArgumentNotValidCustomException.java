package com.app.exception.validation;

import com.app.dto.v1.error.ErrorDetail;
import com.app.dto.v1.error.Links;
import com.app.dto.v1.error.Source;
import com.app.exception.BaseListException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodArgumentNotValidCustomException extends BaseListException {
    private final MethodArgumentNotValidException ex;
    private final String link;

    public MethodArgumentNotValidCustomException( MethodArgumentNotValidException ex, String link) {
        this.ex = ex;
        this.link = link;
    }

    private String determineCauseException() {
        boolean isRequestBody = this.ex.getParameter()
                .hasParameterAnnotation(RequestBody.class);
        return isRequestBody ? "body" : "query_params";
    }

    private String getMessageError(FieldError error) {
        if (Objects.equals(error.getCode(), "typeMismatch")) {
            return String.format("The value '%s' is not valid for the field '%s'",
                    error.getRejectedValue(), error.getField());
        }

        return error.getDefaultMessage();
    }

    @Override
    protected List<ErrorDetail> getListErrorDetail() {
        return ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                            return ErrorDetail
                                    .builder()
                                    .status("UNPROCESSABLE_CONTENT")
                                    .code(HttpStatus.UNPROCESSABLE_CONTENT.value())
                                    .title("Invalid request")
                                    .detail(error.getField() + ": " + getMessageError(error))
                                    .source(new Source(determineCauseException() + "." + error.getField()))
                                    .links(new Links(this.link))
                                    .build();
                        }
                )
                .collect(Collectors.toList());
    }
}
