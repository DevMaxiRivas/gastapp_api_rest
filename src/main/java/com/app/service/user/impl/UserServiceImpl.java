package com.app.service.user.impl;

import com.app.dto.v1.user.QueryParamsUserFilterDTO;
import com.app.dto.v1.user.UserResponseDTO;
import com.app.exception.resource.ResourceNotFoundCustomException;
import com.app.mapper.user.UserMapper;
import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.service.user.UserService;
import com.app.specification.user.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final UserMapper mapper;

    @Override
    public User findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User Not Found", "url.param"));
    }

    @Override
    public User findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User Not Found", "url.param"));
    }

    public Page<UserResponseDTO> getUsersPageable(Pageable pageable) {
        Page<User> entityPage = repo.findAll(pageable);
        return entityPage.map(mapper::toDto);
    }

    public Page<UserResponseDTO> getFilteredUsersPageable(QueryParamsUserFilterDTO filters, Pageable pageable) {
        Specification<User> spec = UserSpecification.filterBy(filters);

        return repo.findAll(spec, pageable)
                .map(mapper::toDto);
    }
}