package com.app.service.category;

import com.app.model.Category;
import com.app.model.User;

import java.util.List;

public interface CategoryService {
    List<Category> getAvailableCategories(Long userId);
    Category createCustomCategory(Category category, User user);
}
