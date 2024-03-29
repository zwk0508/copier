package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:42
 */

public class EnumToStringConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return Enum.class.isAssignableFrom(from) && String.class.equals(to);
    }

    @Override
    public Object convert(Object from, Class<?> to) {
        return ((Enum) from).name();
    }
}
