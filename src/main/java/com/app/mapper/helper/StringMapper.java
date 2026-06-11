package com.app.mapper.helper;

import com.app.mapper.qualifier.LowerCase;
import com.app.mapper.qualifier.Trim;
import com.app.mapper.qualifier.UpperCase;
import org.springframework.stereotype.Component;

@Component
public class StringMapper {

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
}