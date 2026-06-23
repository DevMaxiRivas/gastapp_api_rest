package com.app.dto.v1.transaction;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionUpdateDTO(
        @Positive
        BigDecimal amount,

        LocalDate transactionDate,

        String note,

        Long categoryId
) {}
