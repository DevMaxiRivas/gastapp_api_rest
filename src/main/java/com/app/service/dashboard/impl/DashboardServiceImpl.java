package com.app.service.dashboard.impl;

import com.app.dto.v1.dashboard.SummaryDTO;
import com.app.dto.v1.dashboard.transaction.QueryParamsFilterDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.SummaryBudgetDTO;
import com.app.dto.v1.dashboard.transaction.TransactionDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.TransactionHistoryByMonthDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.enums.transaction.TransactionTypeEnum;
import com.app.model.User;
import com.app.service.dashboard.DashboardService;
import com.app.service.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1),
                LocalDate.now()
        );

        List<TransactionResponseDTO> lastTransactions = transactionService.getRecentTransactions(user.getId(),5);

        if(historyTransactionByMonth.isEmpty()){
            return new SummaryDTO(null, lastTransactions);
        }

        return new SummaryDTO(historyTransactionByMonth.get(0), lastTransactions);
    }

    @Override
    public SummaryBudgetDTO getBudgetSummary(User user) {
        return new SummaryBudgetDTO(
                transactionService.getTotalAmount(
                        user.getId(),
                        LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1),
                        LocalDate.now(),
                        TransactionTypeEnum.EXPENSE
                )
        );
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
