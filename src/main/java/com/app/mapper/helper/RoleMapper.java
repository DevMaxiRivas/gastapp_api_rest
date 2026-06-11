package com.app.mapper.helper;

import com.app.model.User;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public String map(User user) {
        return user == null ? null : user.getRole().getName();
    }
}
