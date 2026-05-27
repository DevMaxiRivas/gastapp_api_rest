package com.app.dto.v1.auth;

import com.app.enums.token.TokenType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    private String token;
    private TokenType tokenType;
}
