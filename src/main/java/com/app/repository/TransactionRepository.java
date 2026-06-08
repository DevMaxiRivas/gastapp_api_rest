package com.app.repository;

import com.app.enums.transaction.TransactionTypeEnum;
import com.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);
    List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDate start, LocalDate end);
    List<Transaction> findByUserIdAndType(Long userId, TransactionTypeEnum type);
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);
}