package com.app.service.user.impl;

import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no registrado"));
    }

    @Override
    @Transactional
    public User updateMonthlyBudget(Long userId, BigDecimal newBudget) {
        User user = findById(userId);
        user.setMonthlyBudget(newBudget);
        return userRepository.save(user);
    }
}