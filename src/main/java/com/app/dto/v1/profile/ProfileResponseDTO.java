package com.app.dto.v1.profile;

import com.app.enums.user.CurrencyType;

import java.math.BigDecimal;

public record ProfileResponseDTO(
        CurrencyType currency,
        BigDecimal currentBudget,
        String avatarUrl
) {}
