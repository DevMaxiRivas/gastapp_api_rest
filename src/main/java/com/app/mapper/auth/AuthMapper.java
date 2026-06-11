package com.app.mapper.auth;

import com.app.dto.v1.auth.RegisterRequest;
import com.app.dto.v1.auth.claim.TokenClaimDTO;
import com.app.mapper.helper.StringMapper;
import com.app.mapper.qualifier.LowerCase;
import com.app.mapper.qualifier.Trim;
import com.app.model.User;
import com.app.util.StorageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = { StorageUtils.class},
        uses = StringMapper.class

)
public interface AuthMapper {
    @Mapping(target = "profile.avatarUrl", expression = "java(StorageUtils.generateURLPublic(profile.getAvatarUrl()))")
    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    TokenClaimDTO toDto(User user);

    @Mapping(
        target = "email",
        source = "email",
        qualifiedBy = {LowerCase.class, Trim.class}
    )
    User toEntity(RegisterRequest dto);

}