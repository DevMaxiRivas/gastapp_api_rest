package com.app.dto.v1.auth;

import com.app.enums.token.TokenTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Token {
    private String value;
    private TokenTypeEnum tokenType;
}
