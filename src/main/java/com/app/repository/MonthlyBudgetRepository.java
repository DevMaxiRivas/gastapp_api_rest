package com.app.repository;

import com.app.model.MonthlyBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Long> {

    Optional<MonthlyBudget> findByUserIdAndBudgetMonth(Long userId, Short month);
    List<MonthlyBudget> findByUserIdOrderByBudgetMonthDesc(Long userId);
}