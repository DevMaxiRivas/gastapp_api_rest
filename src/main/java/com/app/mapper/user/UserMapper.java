package com.app.mapper.user;

import com.app.dto.v1.user.UserResponseDTO;
import com.app.model.User;
import com.app.util.MaskingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = {MaskingUtils.class})
public interface UserMapper {

    @Mapping(target = "email", expression = "java(MaskingUtils.maskEmail(user.getEmail()))")
    UserResponseDTO toDto(User user);

    User toEntity(UserResponseDTO dto);

    List<UserResponseDTO> toDtoList(List<User> users);
}