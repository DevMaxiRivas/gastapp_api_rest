package com.app.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private T data;
    private int statusCode;
    private boolean success;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, HttpStatus.OK.value(), true);
    }
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(data, HttpStatus.CREATED.value(), true);
    }

    public static <T> ApiResponse<T> noContent(int statusCode) {
        return new ApiResponse<>(null, HttpStatus.NO_CONTENT.value(), true);
    }
}
