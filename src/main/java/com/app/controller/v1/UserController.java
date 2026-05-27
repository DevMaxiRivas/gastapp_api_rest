package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.user.UserResponseDTO;
import com.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.success(userService.getUsers())
                );
    }
}
