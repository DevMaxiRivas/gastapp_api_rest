package com.app.dto.v1.dashboard.transaction;

import java.math.BigDecimal;

public record SummaryBudgetDTO(
        BigDecimal totalExpenses
) {}
