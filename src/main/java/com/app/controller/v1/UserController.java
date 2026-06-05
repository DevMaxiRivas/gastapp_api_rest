package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.user.QueryParamsUserFilterDTO;
import com.app.dto.v1.user.UserResponseDTO;
import com.app.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('USERS_READ_ALL')")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsers(
            @Valid QueryParamsUserFilterDTO filters,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.paginatedSuccess(
                                userService.getFilteredUsersPageable(filters, pageable)
                        )
                );
    }
}
