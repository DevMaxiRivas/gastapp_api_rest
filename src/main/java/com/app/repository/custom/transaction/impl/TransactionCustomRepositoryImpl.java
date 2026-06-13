package com.app.repository.custom.transaction.impl;

import com.app.dto.v1.dashboard.transaction.QueryParamsFilterDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.TransactionDailyBalanceDTO;
import com.app.enums.transaction.TransactionTypeEnum;
import com.app.model.Transaction;
import com.app.repository.custom.transaction.TransactionCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.List;

public class TransactionCustomRepositoryImpl implements TransactionCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TransactionDailyBalanceDTO> getDailyBalance(Long userId, QueryParamsFilterDailyBalanceDTO filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionDailyBalanceDTO> query = cb.createQuery(TransactionDailyBalanceDTO.class);
        Root<Transaction> root = query.from(Transaction.class);

        // 1. Definir el SELECT con el constructor del DTO
        // Importante: El orden debe coincidir con el constructor del DTO
        query.select(cb.construct(
                TransactionDailyBalanceDTO.class,
                root.get("transactionDate"),
                cb.sum(
                        cb.selectCase()
                                .when(cb.equal(root.get("type"), "INCOME"), root.get("amount"))
                                .otherwise(BigDecimal.ZERO)
                                .as(BigDecimal.class)
                ),
                cb.sum(
                        cb.selectCase()
                                .when(cb.equal(root.get("type"), "EXPENSE"), root.get("amount"))
                                .otherwise(BigDecimal.ZERO)
                                .as(BigDecimal.class)
                )
        ));

        // 2. Cláusula WHERE
        Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);
        Predicate datePredicate = cb.between(root.get("transactionDate"), filter.fromDate(), filter.toDate());
        query.where(cb.and(userPredicate, datePredicate));

        // 3. GROUP BY y ORDER BY
        query.groupBy(root.get("transactionDate"));
        query.orderBy(cb.desc(root.get("transactionDate")));

        return entityManager.createQuery(query).getResultList();
    }
}
