package com.app.repository.custom.transaction.impl;

import com.app.dto.v1.dashboard.transaction.QueryParamsFilterDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.TransactionDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.TransactionHistoryByMonthDTO;
import com.app.enums.transaction.TransactionTypeEnum;
import com.app.model.Transaction;
import com.app.repository.custom.transaction.TransactionCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionCustomRepositoryImpl implements TransactionCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TransactionDailyBalanceDTO> getDailyBalance(Long userId, QueryParamsFilterDailyBalanceDTO filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionDailyBalanceDTO> query = cb.createQuery(TransactionDailyBalanceDTO.class);
        Root<Transaction> root = query.from(Transaction.class);

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

        // WHERE
        Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);
        Predicate datePredicate = cb.between(root.get("transactionDate"), filter.fromDate(), filter.toDate());
        query.where(cb.and(userPredicate, datePredicate));

        // 3. GROUP BY y ORDER BY
        query.groupBy(root.get("transactionDate"));
        query.orderBy(cb.desc(root.get("transactionDate")));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<TransactionHistoryByMonthDTO> getTransactionHistoryByMonth(
            Long userId,
            LocalDate fromDate,
            LocalDate toDate
    ){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionHistoryByMonthDTO> cq = cb.createQuery(TransactionHistoryByMonthDTO.class);
        Root<Transaction> transaction = cq.from(Transaction.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(transaction.get("user").get("id"), userId));
        predicates.add(cb.between(transaction.get("transactionDate"), fromDate, toDate));

        cq.where(predicates.toArray(new Predicate[0]));

        Expression<Integer> yearExpression = cb.function(
                "date_part",
                Integer.class,
                cb.literal("year"),
                transaction.get("transactionDate")
        );

        Expression<Integer> monthExpression = cb.function(
                "date_part",
                Integer.class,
                cb.literal("month"),
                transaction.get("transactionDate")
        );

        cq.groupBy(yearExpression, monthExpression);

        cq.select(cb.construct(TransactionHistoryByMonthDTO.class,
                yearExpression, monthExpression,
                cb.sum(
                    cb.selectCase()
                        .when(
                                cb.equal(transaction.get("type"), TransactionTypeEnum.INCOME), transaction.get("amount")
                        )
                        .otherwise(
                                cb.literal(BigDecimal.ZERO)
                        )
                        .as(BigDecimal.class)
                ),
                cb.sum(
                        cb.selectCase()
                                .when(
                                        cb.equal(transaction.get("type"), TransactionTypeEnum.EXPENSE), transaction.get("amount")
                                )
                                .otherwise(
                                        cb.literal(BigDecimal.ZERO)
                                )
                                .as(BigDecimal.class)
                )
        ));

        return entityManager.createQuery(cq).getResultList();
    }


    @Override
    public BigDecimal getTotalAmount(Long userId, LocalDate fromDate, LocalDate toDate, TransactionTypeEnum type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Transaction> root = query.from(Transaction.class);

        query.select(
            cb.sum(root.get("amount"))
        );

        ArrayList<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("user").get("id"), userId));
        predicates.add(cb.between(root.get("transactionDate"), fromDate, toDate));
        predicates.add(cb.equal(root.get("type"), type));

        query.where(cb.and(predicates));

        List<BigDecimal> result = entityManager.createQuery(query).getResultList();
        return result.isEmpty() ? BigDecimal.ZERO : result.get(0);
    }


}
