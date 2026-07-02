package com.app.repository;

import com.app.dto.v1.dashboard.transaction.TransactionHistoryByMonthDTO;
import com.app.enums.transaction.TransactionTypeEnum;
import com.app.model.Transaction;
import com.app.repository.custom.transaction.TransactionCustomRepository;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction>
        , TransactionCustomRepository
{
    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);
    List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDate start, LocalDate end);
    List<Transaction> findByUserId(Long userId, Sort sort, Limit limit);
    boolean existsByIdAndUserId(Long id, Long userId);
    List<Transaction> findByUserIdAndType(Long userId, TransactionTypeEnum type);
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);
}