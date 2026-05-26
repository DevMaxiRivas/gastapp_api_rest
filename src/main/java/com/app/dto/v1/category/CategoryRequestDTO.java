package com.app.dto.v1.category;

public record CategoryRequestDTO(
        Long id,
        String name,
        String icon,
        Long userId
) {}