package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:43
 */

public class ObjectToStringConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return Object.class.isAssignableFrom(from) && String.class.equals(to);
    }

    @Override
    public Object convert(Object from, Class<?> to) {
        return String.valueOf(from);
    }
}
