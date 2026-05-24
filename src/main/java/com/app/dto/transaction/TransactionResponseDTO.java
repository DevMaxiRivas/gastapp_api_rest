package com.app.dto.user;

public record TransactionResponseDTO(Long id, BigDecimal amount, String type, LocalDate date, String note, CategoryDTO category) {}