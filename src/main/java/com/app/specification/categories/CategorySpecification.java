package com.app.specification.categories;

import com.app.dto.v1.category.QueryParamsCategoryFilterDTO;
import com.app.model.Category;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CategorySpecification {
    public static Specification<Category> filterBy(QueryParamsCategoryFilterDTO filters, Long userId) {
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

            if (filters.name() != null && !filters.name().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")), "%" + filters.name().toLowerCase() + "%"
                        )
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