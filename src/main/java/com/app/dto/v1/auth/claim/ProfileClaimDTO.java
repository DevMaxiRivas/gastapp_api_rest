package com.app.dto.v1.auth.claim;

import com.app.enums.user.CurrencyType;

import java.math.BigDecimal;

public record ProfileClaimDTO (
        CurrencyType currency,
        BigDecimal currentBudget,
        String avatarUrl
){
}
