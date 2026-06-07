package com.app.util;

import com.app.exception.GenericErrorException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static Map<String, Object> convertToMap(Object obj) {
        try {
            Map<String, Object> map = new HashMap<>();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
            return map;
        } catch (IllegalAccessException e) {
            throw new GenericErrorException(e);
        }
    }
}
