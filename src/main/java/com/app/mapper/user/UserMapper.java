package com.app.mapper.user;

import com.app.dto.v1.auth.RegisterRequest;
import com.app.dto.v1.user.UserEventDTO;
import com.app.dto.v1.user.UserResponseDTO;
import com.app.event.auth.UserCreatedEvent;
import com.app.mapper.config.GlobalMapperConfig;
import com.app.mapper.helper.StringMapper;
import com.app.mapper.profile.ProfileMapper;
import com.app.mapper.qualifier.string.EncodePassword;
import com.app.mapper.qualifier.string.MaskEmail;
import com.app.mapper.qualifier.string.Normalize;
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
    @Mapping(
            target = "username",
            expression = "java(user.getUsernameApp())"
    )
    UserResponseDTO toDto(User user);

    UserEventDTO toEventDto(User user);

    @Mapping(
            target = "email",
            source = "email",
            qualifiedBy = Normalize.class
    )
    @Mapping(
            target = "password",
            source = "password",
            qualifiedBy = EncodePassword.class
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterRequest dto);

    List<UserResponseDTO> toDtoList(List<User> users);
}