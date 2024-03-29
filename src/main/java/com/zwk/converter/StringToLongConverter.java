package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:30
 */

public class StringToLongConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && (Long.class.equals(to) || long.class.equals(to));
    }

    @Override
    public Object convert(Object from, Class<?> _) {
        return Long.parseLong((String) from);
    }
}
