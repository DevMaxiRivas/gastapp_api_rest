package com.app.service.user.impl;

import com.app.dto.v1.auth.RegisterRequest;
import com.app.dto.v1.user.QueryParamsUserFilterDTO;
import com.app.dto.v1.user.UserResponseDTO;
import com.app.exception.body.ValidationRequestBodyCustomException;
import com.app.exception.resource.ResourceNotFoundCustomException;
import com.app.mapper.user.UserMapper;
import com.app.model.Role;
import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.service.role.RoleService;
import com.app.service.user.UserService;
import com.app.specification.user.UserSpecification;
import com.app.util.HashingUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final UserMapper mapper;
    private final RoleService roleService;

    @Override
    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public Page<UserResponseDTO> getUsersPageable(Pageable pageable) {
        Page<User> entityPage = repo.findAll(pageable);
        return entityPage.map(mapper::toDto);
    }

    @Override
    public Page<UserResponseDTO> getFilteredUsersPageable(QueryParamsUserFilterDTO filters, Pageable pageable) {
        Specification<User> spec = UserSpecification.filterBy(filters);

        return repo.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public User create(RegisterRequest dto) {
        if (repo.existsByEmail(dto.email())) throw new ValidationRequestBodyCustomException("email: is already in use", "body.email" );
        if (repo.existsByUsername(dto.username())) throw new ValidationRequestBodyCustomException("username: is already in use", "body.username" );

        User user = mapper.toEntity(dto);
        Role defaultRole = roleService.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(defaultRole);
        repo.save(user);
        return user;
    }

    @Override
    @Transactional
    public void addRefreshToken(User user, String hashToken) {
        List<String> currentTokens = user.getTokens() != null ? user.getTokens() : new ArrayList<String>();
        currentTokens.add(hashToken);

        user.setTokens(currentTokens);
        repo.save(user);
    }

    @Override
    @Transactional
    public void removeRefreshToken(User user, String hashToken) {
        repo.removeToken(user.getId(), hashToken);
    }


}