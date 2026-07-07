package com.app.dto.v1.email;

import jakarta.annotation.Nullable;

public record SendEmailDTO(
        String to,

        String recipientName,

        String subject
){}
