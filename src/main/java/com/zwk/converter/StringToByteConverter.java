package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:28
 */

public class StringToByteConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && (Byte.class.equals(to) || byte.class.equals(to));
    }

    @Override
    public Object convert(Object from, Class<?> _) {
        return Byte.parseByte((String) from);
    }
}
