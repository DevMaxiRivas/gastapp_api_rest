package com.app.dto.v1.transaction;

import com.app.enums.transaction.TransactionTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionCreateDTO(
        @NotNull
        @Positive
        BigDecimal amount,

        TransactionTypeEnum type,

        @NotNull
        LocalDate transactionDate,

        String note,

        @NotNull
        Long categoryId
) {}
