package com.app.dto.v1.dashboard.transaction;

import com.app.enums.transaction.TransactionTypeEnum;

import java.math.BigDecimal;

public record TransactionHistoryByMonthDTO(
    int year,
    int month,
    TransactionTypeEnum type,
    BigDecimal amount
){}
