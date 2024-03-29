package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:28
 */

public class StringToBooleanConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && (Boolean.class.equals(to) || boolean.class.equals(to));
    }

    @Override
    public Object convert(Object from, Class<?> _) {
        return Boolean.parseBoolean((String) from);
    }

}
