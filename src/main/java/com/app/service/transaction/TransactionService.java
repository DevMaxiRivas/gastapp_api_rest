package com.app.service.transaction;

import com.app.dto.v1.transaction.TransactionCreateDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.model.Transaction;
import com.app.model.User;

import java.util.List;

public interface TransactionService {
    TransactionResponseDTO create(TransactionCreateDTO dto, User user);
    List<Transaction> getMonthlyTransactions(Long userId, int month, int year);
    void delete(Long id);
}