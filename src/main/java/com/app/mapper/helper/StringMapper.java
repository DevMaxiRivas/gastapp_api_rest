package com.app.mapper.helper;

import com.app.mapper.qualifier.string.*;
import org.springframework.stereotype.Component;

@Component
public class StringMapper {

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
}