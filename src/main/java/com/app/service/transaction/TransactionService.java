package com.app.service.transaction;

import com.app.dto.v1.dashboard.TransactionHistoryByMonthDTO;
import com.app.dto.v1.transaction.QueryParamsTransactionFilterDTO;
import com.app.dto.v1.transaction.TransactionCreateDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.model.Transaction;
import com.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransactionResponseDTO create(TransactionCreateDTO dto, User user);
    List<Transaction> getMonthlyTransactions(Long userId, int month, int year);
    void delete(Long id);
    Page<TransactionResponseDTO> getFilteredPageable(QueryParamsTransactionFilterDTO filters, Pageable pageable, User user);
    List<TransactionResponseDTO> getRecentTransactions(Long userId, int quantity);

    List<TransactionHistoryByMonthDTO>  getTransactionHistoryByMonth(Long userId, LocalDate startDate, LocalDate endDate);

}