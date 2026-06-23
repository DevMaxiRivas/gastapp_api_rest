package com.app.mapper.transaction;

import com.app.dto.v1.transaction.TransactionCreateDTO;
import com.app.dto.v1.transaction.TransactionResponseDTO;
import com.app.dto.v1.transaction.TransactionUpdateDTO;
import com.app.mapper.category.CategoryMapper;
import com.app.mapper.config.GlobalMapperConfig;
import com.app.model.Transaction;
import org.mapstruct.*;

@Mapper(
    config= GlobalMapperConfig.class,
    uses = {CategoryMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TransactionMapper {
    TransactionResponseDTO toDto(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "type", ignore = true)
    Transaction toEntity(TransactionCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "type", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction toEntity(TransactionUpdateDTO dto, @MappingTarget Transaction target);

}