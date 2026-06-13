package com.app.service.dashboard.impl;

import com.app.dto.v1.dashboard.SummaryDTO;
import com.app.dto.v1.dashboard.TransactionHistoryByMonthDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.model.User;
import com.app.service.dashboard.DashboardService;
import com.app.service.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final TransactionService transactionService;


    @Override
    public SummaryDTO getSummary(User user) {
        List<TransactionHistoryByMonthDTO> historyTransactionByMonth = transactionService.getTransactionHistoryByMonth(
                user.getId(),
                LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()),
                LocalDate.now()
        );

        List<TransactionResponseDTO> lastTransactions = transactionService.getRecentTransactions(user.getId(),5);

        return new SummaryDTO(historyTransactionByMonth, lastTransactions);
    }
}
