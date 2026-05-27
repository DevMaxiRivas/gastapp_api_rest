package com.app.service.user;

import com.app.dto.v1.user.UserResponseDTO;
import com.app.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User findById(Long id);
    User findByEmail(String email);
    User updateMonthlyBudget(Long userId, BigDecimal newBudget);
    List<UserResponseDTO> getUsers();
}