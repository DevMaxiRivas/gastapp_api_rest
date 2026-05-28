package com.app.specification.user;

import com.app.dto.v1.user.QueryParamsUserFilterDTO;
import com.app.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filterBy(QueryParamsUserFilterDTO filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.currency() != null) {
                predicates.add(
                        cb.equal(
                                root.get("currency"), filters.currency()
                        )
                );
            }

            if (filters.name() != null && !filters.name().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")), "%" + filters.name().toLowerCase() + "%"
                        )
                );
            }

//            if (filters.categoryId() != null) {
//                predicates.add(cb.equal(root.get("category").get("id"), filters.categoryId()));
//            }

            if (filters.minMonthlyBudget() != null) {
                predicates.add(
                        cb.ge(
                                root.get("monthlyBudget"), filters.minMonthlyBudget()
                        )
                );
            }

            if (filters.maxMonthlyBudget() != null) {
                predicates.add(
                        cb.le(
                                root.get("monthlyBudget"), filters.maxMonthlyBudget()
                        )
                );
            }

            if (filters.startCreatedAt() != null && filters.endCreatedAt() != null) {
                predicates.add(
                        cb.between(
                                root.get("createdAt"), filters.endCreatedAt(), filters.endCreatedAt()
                        )
                );
            } else {
                if (filters.startCreatedAt() != null) {
                    predicates.add(
                            cb.greaterThan(
                                    root.get("createdAt"), filters.startCreatedAt()
                            )
                    );
                }

                if (filters.endCreatedAt() != null) {
                    predicates.add(
                            cb.lessThan(
                                    root.get("createdAt"), filters.endCreatedAt()
                            )
                    );
                }
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}