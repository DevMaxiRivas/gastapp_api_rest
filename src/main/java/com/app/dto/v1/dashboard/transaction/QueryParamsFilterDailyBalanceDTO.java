package com.app.dto.v1.dashboard.transaction;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record QueryParamsFilterDailyBalanceDTO (
        @NotNull
        LocalDate fromDate,

        @NotNull
        LocalDate toDate,

        Long categoryId,

        Long userId
) {}
