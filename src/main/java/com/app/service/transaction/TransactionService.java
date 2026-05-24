package com.app.service.transaction;

import com.app.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction create(Transaction transaction, Long userId, Long categoryId);
    List<Transaction> getMonthlyTransactions(Long userId, int month, int year);
    void delete(Long id);
}