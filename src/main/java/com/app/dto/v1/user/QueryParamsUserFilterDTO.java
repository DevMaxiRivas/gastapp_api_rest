package com.app.dto.v1.user;

import com.app.enums.user.CurrencyType;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record QueryParamsUserFilterDTO(

        CurrencyType currency,

        String name,

        String email,

        @PositiveOrZero
        BigDecimal minMonthlyBudget,

        @PositiveOrZero
        BigDecimal maxMonthlyBudget,

        LocalDateTime startCreatedAt,

        LocalDateTime endCreatedAt
) {}
