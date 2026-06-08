package com.app.dto.v1.category;

import com.app.enums.category.CategoryTypeEnum;
import com.app.enums.user.CurrencyType;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record QueryParamsCategoryFilterDTO(
        Long userId,
        String name,
        CategoryTypeEnum type
) {}
