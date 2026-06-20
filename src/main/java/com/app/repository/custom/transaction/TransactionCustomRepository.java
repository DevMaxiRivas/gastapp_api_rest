package com.app.repository.custom.transaction;

import com.app.dto.v1.dashboard.transaction.QueryParamsFilterDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.TransactionDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.TransactionHistoryByMonthDTO;
import com.app.enums.transaction.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionCustomRepository {
    List<TransactionDailyBalanceDTO> getDailyBalance(Long userId, QueryParamsFilterDailyBalanceDTO filter);
    List<TransactionHistoryByMonthDTO> getTransactionHistoryByMonth(Long userId,LocalDate fromDate,LocalDate toDate);
    BigDecimal getTotalAmount(Long userId, LocalDate fromDate, LocalDate toDate, TransactionTypeEnum type);
}