package com.app.service.dashboard.impl;

import com.app.dto.v1.dashboard.SummaryDTO;
import com.app.dto.v1.dashboard.transaction.QueryParamsFilterDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.TransactionDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.TransactionHistoryByMonthDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.model.User;
import com.app.service.dashboard.DashboardService;
import com.app.service.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public List<TransactionDailyBalanceDTO> getDailyBalance(User user, QueryParamsFilterDailyBalanceDTO filter) {
        Long userId = (Objects.equals(user.getRole().getName(),"USER")) ? user.getId() : filter.userId();
        if(userId == null) {
            return List.of();
        }
        return transactionService.getDailyBalance(userId, filter);
    }
}
