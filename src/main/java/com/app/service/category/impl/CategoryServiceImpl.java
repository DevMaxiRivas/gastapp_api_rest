package com.app.service.category.impl;

import com.app.dto.category.CategoryRequestDTO;
import com.app.dto.category.CategoryResponseDTO;
import com.app.mapper.category.CategoryMapper;
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
    private final CategoryRepository repo;
    private final CategoryMapper mapper;

    public List<CategoryResponseDTO> getAvailableCategories(Long userId) {
        return mapper.toDtoList(repo.findAllAvailableToUser(userId));
    }

    @Transactional
    public Category createCustomCategory(CategoryRequestDTO dto, User user) {
        Category category = mapper.toEntity(dto);
        category.setUser(user);
        return repo.save(category);
    }
}
