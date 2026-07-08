package com.app.dto.v1.user;

import java.math.BigInteger;

public record UserEventDTO (
        BigInteger id,
        String email,
        String username
){}
