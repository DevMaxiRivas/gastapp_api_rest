package com.app.dto.v1.transaction;
import com.app.enums.transaction.TransactionTypeEnum;

import java.time.LocalDate;
import java.util.List;

public record QueryParamsTransactionFilterDTO(
        Long userId,
        TransactionTypeEnum type,
        LocalDate startTransactionDate,
        LocalDate endTransactionDate,
        Long minAmount,
        Long maxAmount,
        List<Long> categoryId
) {}
