package com.app.dto.v1;

public record PaginationMeta(
        int currentPage,
        int totalPages,
        long totalElements,
        int pageSize,
        boolean isFirst,
        boolean isLast
) {}