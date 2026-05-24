package com.app.mapper.user;

import com.app.dto.user.UserResponseDTO;
import com.app.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDto(User user);
    User toEntity(UserResponseDTO dto);
}