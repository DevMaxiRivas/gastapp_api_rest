package com.app.dto.v1.dashboard;

import com.app.enums.transaction.TransactionTypeEnum;

import java.math.BigDecimal;

public record TransactionHistoryByMonthDTO(
    int year,
    int month,
    TransactionTypeEnum type,
    BigDecimal amount
){}
