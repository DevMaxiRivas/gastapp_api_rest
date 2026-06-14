package com.app.service.dashboard;

import com.app.dto.v1.dashboard.SummaryDTO;
import com.app.dto.v1.dashboard.transaction.QueryParamsFilterDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.SummaryBudgetDTO;
import com.app.dto.v1.dashboard.transaction.TransactionDailyBalanceDTO;
import com.app.model.User;

import java.util.List;

public interface DashboardService {
    SummaryDTO getSummary(User user);
    SummaryBudgetDTO getBudgetSummary(User user);
    List<TransactionDailyBalanceDTO> getDailyBalance(User user, QueryParamsFilterDailyBalanceDTO filter);
}
