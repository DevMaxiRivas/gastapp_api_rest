package com.app.dto.v1.transaction;

import com.app.enums.transaction.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(
        @NotNull
        @Min(0)
        BigDecimal amount,

        TransactionType type,

        @NotNull
        LocalDate date,

        String note,

        @NotNull
        Long categoryId
) {}
