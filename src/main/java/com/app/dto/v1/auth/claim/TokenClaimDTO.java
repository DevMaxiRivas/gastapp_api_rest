package com.app.dto.v1.auth.claim;


public record TokenClaimDTO(
        Long id,
        String name,
        String email,
        ProfileClaimDTO profile,
        String role
) {}