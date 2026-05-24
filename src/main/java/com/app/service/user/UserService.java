package com.app.service.user;

import com.app.model.User;

import java.math.BigDecimal;

public interface UserService {
    User findById(Long id);
    User findByEmail(String email);
    User updateMonthlyBudget(Long userId, BigDecimal newBudget);
}