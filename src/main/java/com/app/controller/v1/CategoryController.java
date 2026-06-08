package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.category.CategoryResponseDTO;
import com.app.dto.v1.category.QueryParamsCategoryFilterDTO;
import com.app.model.User;
import com.app.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getCategories(
            @AuthenticationPrincipal User user,
            @Valid QueryParamsCategoryFilterDTO filters,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ){
        return ResponseEntity.ok(
                ApiResponse.paginatedSuccess(
                        categoryService.getFilteredPageable(filters, pageable, user)
                )
        );
    }
}
