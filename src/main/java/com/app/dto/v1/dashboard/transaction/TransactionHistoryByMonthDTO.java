package com.app.dto.v1.dashboard.transaction;

import java.math.BigDecimal;

public record TransactionHistoryByMonthDTO(
    Integer year,
    Integer month,
    BigDecimal totalIncome,
    BigDecimal totalExpense
){}
