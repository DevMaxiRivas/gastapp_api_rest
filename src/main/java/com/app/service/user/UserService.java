package com.app.service.user;

import com.app.dto.v1.auth.RegisterRequest;
import com.app.dto.v1.user.QueryParamsUserFilterDTO;
import com.app.dto.v1.user.UserResponseDTO;
import com.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Page<UserResponseDTO> getUsersPageable(Pageable pageable);
    Page<UserResponseDTO> getFilteredUsersPageable(QueryParamsUserFilterDTO filters, Pageable pageable);
    User create(RegisterRequest dto);
    void addRefreshToken(User user, String hashToken);
    void removeRefreshToken(User user, String hashToken);
}