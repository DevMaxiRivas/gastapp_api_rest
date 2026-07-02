package com.app.dto.v1.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionCreateDTO(
        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        LocalDate transactionDate,

        String note,

        Long userId,

        @NotNull
        Long categoryId
) {}
