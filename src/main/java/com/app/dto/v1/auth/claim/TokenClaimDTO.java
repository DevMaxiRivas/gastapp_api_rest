package com.app.dto.v1.auth.claim;


public record TokenClaimDTO(
        Long id,
        String username,
        String email,
        ProfileClaimDTO profile,
        String role
) {}