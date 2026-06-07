package com.app.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private T data;
    private PaginationMeta meta;
    private int statusCode;
    private final boolean success = true;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data,null, HttpStatus.OK.value());
    }

    public static <T> ApiResponse<List<T>> paginatedSuccess(Page<T> page) {
        PaginationMeta meta = new PaginationMeta(
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.isFirst(),
                page.isLast()
        );
        return new ApiResponse<>(page.getContent(), meta, HttpStatus.OK.value());
    }


    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(data, null, HttpStatus.CREATED.value());
    }

    public static <T> ApiResponse<T> noContent(int statusCode) {
        return new ApiResponse<>(null, null, HttpStatus.NO_CONTENT.value());
    }
}
