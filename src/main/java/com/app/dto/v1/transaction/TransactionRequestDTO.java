package com.app.dto.v1.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(BigDecimal amount, String type, LocalDate date, String note, Long categoryId) {}
