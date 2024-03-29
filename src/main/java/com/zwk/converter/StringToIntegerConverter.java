package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:25
 */

public class StringToIntegerConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && (Integer.class.equals(to) || int.class.equals(to));
    }

    @Override
    public Object convert(Object from, Class<?> _) {
        return Integer.parseInt((String) from);
    }
}
