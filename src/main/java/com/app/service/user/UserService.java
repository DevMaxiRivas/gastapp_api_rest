package com.app.service.user;

import com.app.dto.v1.user.QueryParamsUserFilterDTO;
import com.app.dto.v1.user.UserResponseDTO;
import com.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface UserService {
    User findById(Long id);
    User findByEmail(String email);
    Page<UserResponseDTO> getUsersPageable(Pageable pageable);
    Page<UserResponseDTO> getFilteredUsersPageable(QueryParamsUserFilterDTO filters, Pageable pageable);
}