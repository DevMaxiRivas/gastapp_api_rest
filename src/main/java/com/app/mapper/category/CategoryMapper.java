package com.app.mapper.category;

import com.app.dto.category.CategoryRequestDTO;
import com.app.dto.category.CategoryResponseDTO;
import com.app.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDTO toDto(Category category);

    @Mapping(target = "user", ignore = true)
    Category toEntity(CategoryRequestDTO dto);

    List<CategoryResponseDTO> toDtoList(List<Category> categories);
}