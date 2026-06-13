package com.app.dto.v1.dashboard;

import com.app.dto.v1.transaction.TransactionResponseDTO;

import java.util.List;

public record SummaryDTO (
        List<TransactionHistoryByMonthDTO> historyByMonth,
        List<TransactionResponseDTO> recentTransactions
){}
