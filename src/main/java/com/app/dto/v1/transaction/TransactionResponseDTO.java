package com.app.dto.v1.transaction;

import com.app.dto.v1.category.CategoryResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(Long id, BigDecimal amount, String type, LocalDate date, String note, CategoryResponseDTO category) {}