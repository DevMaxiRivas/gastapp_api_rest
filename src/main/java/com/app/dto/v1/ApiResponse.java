package com.app.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private T data;
    private int statusCode;
    private boolean success;

    public static <T> ApiResponse<T> success(T data, int statusCode) {
        return new ApiResponse<>(data, statusCode, true);
    }
}
