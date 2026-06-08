package com.app.dto.v1.transaction;

import com.app.enums.transaction.TransactionTypeEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionCreateDTO(
        @NotNull
        @Min(0)
        BigDecimal amount,

        TransactionTypeEnum type,

        @NotNull
        LocalDate transactionDate,

        String note,

        @NotNull
        Long categoryId
) {}
