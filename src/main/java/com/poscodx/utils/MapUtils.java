package com.poscodx.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapUtils {

    public static Map<String, Object> toMap(Object obj) {
        return Arrays.stream(obj.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toMap(
                        Field::getName,
                        field -> {
                            try {
                                return Objects.isNull(field.get(obj)) ? "null" : field.get(obj);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ));
    }
}
