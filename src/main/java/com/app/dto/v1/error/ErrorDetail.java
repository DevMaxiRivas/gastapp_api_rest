package com.app.dto.v1.errors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetail {
    private String status;
    private int code;
    private String title;
    private String detail;
    private Source source;
    private Links links;
}
