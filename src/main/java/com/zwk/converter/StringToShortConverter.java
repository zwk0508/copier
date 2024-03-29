package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:29
 */

public class StringToShortConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && (Short.class.equals(to) || short.class.equals(to));
    }

    @Override
    public Object convert(Object from, Class<?> _) {
        return Short.parseShort((String) from);
    }
}
