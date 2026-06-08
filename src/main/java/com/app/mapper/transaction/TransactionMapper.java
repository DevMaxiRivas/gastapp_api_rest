package com.app.mapper.transaction;

import com.app.dto.v1.transaction.TransactionCreateDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.mapper.category.CategoryMapper;
import com.app.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface TransactionMapper {
    TransactionResponseDTO toDto(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    Transaction toEntity(TransactionCreateDTO dto);

    List<TransactionResponseDTO> toDtoList(List<Transaction> transactions);
}