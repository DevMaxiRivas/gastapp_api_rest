package com.app.dto.v1.dashboard.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDailyBalanceDTO(
        LocalDate date,
        BigDecimal totalIncome,
        BigDecimal totalExpense
){}
