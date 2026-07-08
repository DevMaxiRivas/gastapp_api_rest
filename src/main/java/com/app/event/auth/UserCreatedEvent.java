package com.app.event.auth;

import com.app.dto.v1.user.UserEventDTO;

public record UserCreatedEvent (
        UserEventDTO user
) {}
