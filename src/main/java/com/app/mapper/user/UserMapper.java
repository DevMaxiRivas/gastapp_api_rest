package com.app.mapper.user;

import com.app.dto.v1.user.UserResponseDTO;
import com.app.model.User;
import com.app.util.MaskingUtils;
import com.app.util.StorageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = {MaskingUtils.class, StorageUtils.class})
public interface UserMapper {

    @Mapping(target = "email", expression = "java(MaskingUtils.maskEmail(user.getEmail()))")
    @Mapping(target = "profile.avatarUrl", expression = "java(StorageUtils.generateURLPublic(profile.getAvatarUrl()))")
    UserResponseDTO toDto(User user);

    User toEntity(UserResponseDTO dto);

    List<UserResponseDTO> toDtoList(List<User> users);
}