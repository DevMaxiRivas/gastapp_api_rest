package com.app.dto.v1.user;

import com.app.dto.v1.profile.ProfileResponseDTO;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        ProfileResponseDTO profile
) {}