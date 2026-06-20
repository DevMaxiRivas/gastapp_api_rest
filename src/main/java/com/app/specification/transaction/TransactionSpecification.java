package com.app.specification.transaction;

import com.app.dto.v1.transaction.QueryParamsTransactionFilterDTO;
import com.app.model.Transaction;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionSpecification {
    public static Specification<Transaction> filterBy(QueryParamsTransactionFilterDTO filters, Long userId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                List<Predicate> userCondition = new ArrayList<>();
                userCondition.add(
                        cb.equal(
                                root.get("user").get("id"), userId
                        )
                );
                userCondition.add(
                        cb.isNull(
                                root.get("user").get("id")
                        )
                );
                predicates.add(
                        cb.or(userCondition)
                );
            }

            if (filters.type() != null) {
                predicates.add(
                        cb.equal(
                                root.get("type"), filters.type()
                        )
                );
            }

            if (filters.noteContains() != null && !filters.noteContains().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("note")), "%" + filters.noteContains().toLowerCase() + "%"
                        )
                );
            }

            if (filters.startTransactionDate() != null && filters.endTransactionDate() != null) {
                predicates.add(
                        cb.between(
                                root.get("createdAt"), filters.endTransactionDate(), filters.endTransactionDate()
                        )
                );
            } else {
                if (filters.startTransactionDate() != null) {
                    predicates.add(
                            cb.greaterThan(
                                    root.get("createdAt"), filters.startTransactionDate()
                            )
                    );
                }

                if (filters.endTransactionDate() != null) {
                    predicates.add(
                            cb.lessThan(
                                    root.get("createdAt"), filters.endTransactionDate()
                            )
                    );
                }
            }

            if (filters.minAmount() != null) {
                predicates.add(
                        cb.lessThan(
                                root.get("amount"), filters.minAmount()
                        )
                );
            }

            if (filters.maxAmount() != null) {
                predicates.add(
                        cb.lessThan(
                                root.get("amount"), filters.maxAmount()
                        )
                );
            }

            if (filters.categoryId() != null) {
                cb.equal(
                        root.get("category").get("id"), filters.categoryId()
                );
            }

            return cb.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }
}
