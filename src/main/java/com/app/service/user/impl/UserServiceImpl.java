package com.app.service.user.impl;

import com.app.dto.v1.user.UserResponseDTO;
import com.app.mapper.user.UserMapper;
import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final UserMapper mapper;

    @Override
    public User findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public User findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no registrado"));
    }

    @Override
    @Transactional
    public User updateMonthlyBudget(Long userId, BigDecimal newBudget) {
        User user = findById(userId);
        user.setMonthlyBudget(newBudget);
        return repo.save(user);
    }

    public List<UserResponseDTO> getUsers(){
        return mapper.toDtoList(repo.findAll());
    }
}