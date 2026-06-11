package com.app.mapper.user;

import com.app.dto.v1.user.UserResponseDTO;
import com.app.mapper.config.GlobalMapperConfig;
import com.app.mapper.helper.StringMapper;
import com.app.mapper.profile.ProfileMapper;
import com.app.mapper.qualifier.string.MaskEmail;
import com.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
    config= GlobalMapperConfig.class,
    uses = {
        StringMapper.class,
        ProfileMapper.class
    }
)
public interface UserMapper {

    @Mapping(
        target = "email",
        source = "email",
        qualifiedBy = MaskEmail.class
    )
    UserResponseDTO toDto(User user);

//    User toEntity(UserResponseDTO dto);

    List<UserResponseDTO> toDtoList(List<User> users);
}