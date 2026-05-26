package com.app.dto.category;

public record CategoryRequestDTO(
        Long id,
        String name,
        String icon,
        Long userId
) {}