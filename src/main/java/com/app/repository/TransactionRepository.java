package com.app.repository;

import com.app.model.Transaction;
import com.app.repository.custom.transaction.TransactionCustomRepository;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TransactionRepository
        extends
            JpaRepository<Transaction, Long>,
            JpaSpecificationExecutor<Transaction>,
            TransactionCustomRepository
{
    List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDate start, LocalDate end);
    List<Transaction> findByUserId(Long userId, Sort sort, Limit limit);
    boolean existsByIdAndUserId(Long id, Long userId);
}