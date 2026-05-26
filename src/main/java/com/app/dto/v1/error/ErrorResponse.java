package com.app.dto.v1.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String status;
    private List<ErrorDetail> errors;
}

