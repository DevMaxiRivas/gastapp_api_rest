package com.app.dto.transaction;

import com.app.dto.category.CategoryResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(Long id, BigDecimal amount, String type, LocalDate date, String note, CategoryResponseDTO category) {}