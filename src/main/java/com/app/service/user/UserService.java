package com.app.service.user;

import com.app.dto.v1.user.UserResponseDTO;
import com.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User findById(Long id);
    User findByEmail(String email);
    User updateMonthlyBudget(Long userId, BigDecimal newBudget);
    Page<UserResponseDTO> getUsersPageable(Pageable pageable);
}