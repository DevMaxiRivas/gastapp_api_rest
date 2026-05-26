package com.app.dto.v1.errors;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String status = "error";
    private List<ErrorDetail> errors;
}

