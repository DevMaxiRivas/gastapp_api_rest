package com.app.service.category.impl;

import com.app.model.Category;
import com.app.model.User;
import com.app.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl {
    private final CategoryRepository categoryRepository;

    public List<Category> getAvailableCategories(Long userId) {
        return categoryRepository.findAllAvailableToUser(userId);
    }

    @Transactional
    public Category createCustomCategory(Category category, User user) {
        category.setUser(user);
        return categoryRepository.save(category);
    }
}
