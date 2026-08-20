package com.app.units.categories;

import com.app.dto.v1.category.CategoryResponseDTO;
import com.app.dto.v1.category.QueryParamsCategoryFilterDTO;
import com.app.enums.category.CategoryIconEnum;
import com.app.enums.category.CategoryTypeEnum;
import com.app.enums.transaction.TransactionTypeEnum;
import com.app.mapper.category.CategoryMapper;
import com.app.model.Category;
import com.app.model.Role;
import com.app.model.User;
import com.app.repository.CategoryRepository;
import com.app.service.category.impl.CategoryServiceImpl;
import com.app.specification.categories.CategorySpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.ArgumentMatchers;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    CategoryRepository repo;

    @Mock
    CategoryMapper mapper;

    @InjectMocks
    CategoryServiceImpl service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        Role role = new Role(1L, "RoleTest", null);
        // Create test user principal
        User mockUser = new User(
                1L,
                "user-test",
                "user@example.test",
                "$2a$10$Q7sKca.TSnbTnj7xlv0RZu7Bu6Ay6MNRVuFAZcKVBBrfB44lhHWaC",
                List.of(),
                null,
                role
        );

        // Link authentication to the user
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);

        // Set the context holder
        SecurityContextHolder.setContext(securityContext);
    }

    private User getAuthenticatedUser() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if(user == null) throw new IllegalStateException("Authentication object is null");
        return (User) user.getPrincipal();
    }

    @Test
    void testGetAll_ShouldGetAllCategories() {
//        Arrange
        List<Category> categories = List.of(
                new Category(
                  1L, "TestCategory", CategoryIconEnum.MOREHORIZONTALICON, TransactionTypeEnum.EXPENSE, null, LocalDateTime.now()
                ),
                new Category(
                        2L, "TestCategory2", CategoryIconEnum.MOREHORIZONTALICON, TransactionTypeEnum.INCOME, null, LocalDateTime.now()
                )
        );
        Long userId = getAuthenticatedUser().getId();
        QueryParamsCategoryFilterDTO filters = new QueryParamsCategoryFilterDTO(userId, "test", CategoryTypeEnum.EXPENSE);
        Specification<Category> spec = CategorySpecification.filterBy(filters, userId);

        Pageable pageable = PageRequest.of(0,2);
        Page<Category> entityPage = new PageImpl<>(categories, pageable,2);

        Mockito.when(repo.findAll(
                ArgumentMatchers.any(Specification.class),
                ArgumentMatchers.eq(pageable)
        )).thenReturn(entityPage);

        Mockito.when(mapper.toDto(ArgumentMatchers.any(Category.class)))
                .thenAnswer(invocation -> {
                    Category source = invocation.getArgument(0);
                    return new CategoryResponseDTO(
                            source.getId(),
                            source.getName(),
                            source.getType().toString(),
                            source.getIcon().toString()
                    );
                });

        // Act
        Page<CategoryResponseDTO> result = service.getFilteredPageable(
                filters,
                pageable,
                getAuthenticatedUser()
        );

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0)).isNotNull();

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);

        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
        assertThat(result.getContent().get(0).name()).isEqualTo("TestCategory");
    }
}