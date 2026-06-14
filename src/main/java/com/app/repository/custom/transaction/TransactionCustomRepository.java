package com.app.repository.custom.transaction;

import com.app.dto.v1.dashboard.transaction.QueryParamsFilterDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.TransactionDailyBalanceDTO;
import com.app.enums.transaction.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionCustomRepository {
    List<TransactionDailyBalanceDTO> getDailyBalance(Long userId, QueryParamsFilterDailyBalanceDTO filter);
    BigDecimal getTotalAmount(Long userId, LocalDate fromDate, LocalDate toDate, TransactionTypeEnum type);
}