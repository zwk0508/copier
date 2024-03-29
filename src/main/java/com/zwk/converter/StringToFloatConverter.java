package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:31
 */

public class StringToFloatConverter implements Converter{
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && (Float.class.equals(to)||float.class.equals(to));
    }

    @Override
    public Object convert(Object from,Class<?>_) {
        return Float.parseFloat((String) from);
    }
}
