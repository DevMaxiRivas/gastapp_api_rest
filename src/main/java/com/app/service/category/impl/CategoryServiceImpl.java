package com.app.service.category.impl;

import com.app.dto.v1.category.CategoryResponseDTO;
import com.app.dto.v1.category.QueryParamsCategoryFilterDTO;
import com.app.mapper.category.CategoryMapper;
import com.app.model.Category;
import com.app.model.User;
import com.app.repository.CategoryRepository;
import com.app.service.category.CategoryService;
import com.app.specification.categories.CategorySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;
    private  final CategoryMapper mapper;

    public Page<CategoryResponseDTO> getFilteredPageable(QueryParamsCategoryFilterDTO filters, Pageable pageable, User user){
        Long userId = (Objects.equals(user.getRole().getName(),"USER")) ? user.getId() : filters.userId();

        Specification<Category> spec = CategorySpecification.filterBy(filters, userId);
        return repo.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    public List<Category> getAvailableCategories(Long userId) {
        return repo.findAllAvailableToUser(userId);
    }

    @Transactional
    public Category createCustomCategory(Category category, User user) {
        category.setUser(user);
        return repo.save(category);
    }
}