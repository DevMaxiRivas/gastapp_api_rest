package com.app.service.category.impl;

//import com.app.dto.v1.category.CategoryRequestDTO;
//import com.app.dto.v1.category.CategoryResponseDTO;
//import com.app.mapper.category.CategoryMapper;
import com.app.model.Category;
import com.app.model.User;
import com.app.repository.CategoryRepository;
import com.app.service.category.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
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