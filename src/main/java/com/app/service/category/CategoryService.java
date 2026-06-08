package com.app.service.category;

import com.app.dto.v1.category.CategoryResponseDTO;
import com.app.dto.v1.category.QueryParamsCategoryFilterDTO;
import com.app.dto.v1.user.QueryParamsUserFilterDTO;
import com.app.dto.v1.user.UserResponseDTO;
import com.app.model.Category;
import com.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<Category> getAvailableCategories(Long userId);
    Category createCustomCategory(Category category, User user);
    Page<CategoryResponseDTO> getFilteredPageable(QueryParamsCategoryFilterDTO filters, Pageable pageable, User user);
}
