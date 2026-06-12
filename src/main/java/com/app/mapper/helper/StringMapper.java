package com.app.mapper.helper;

import com.app.mapper.qualifier.string.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StringMapper {

    private final PasswordEncoder passwordEncoder;

    @Normalize
    public String normalize(String value){
        return value.toLowerCase().trim();
    }

    @Trim
    public String trim(String value) {
        if(value == null || value.trim().isEmpty()) {
            return value;
        }
        return value.trim();
    }

    @UpperCase
    public String upper(String value) {
        if(value == null || value.trim().isEmpty()) {
            return value;
        }
        return value.toUpperCase();
    }

    @LowerCase
    public String lowerCase(String value) {
        return value == null ? null : value.toLowerCase();
    }

    @MaskEmail
    public String maskEmail(String email) {
        return email.replaceAll("(?<=.{1}).(?=.*@)", "*");
    }

    @EncodePassword
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}