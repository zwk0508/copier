package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:31
 */

public class StringToDoubleConverter implements Converter{
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && (Double.class.equals(to)|| double.class.equals(to));
    }

    @Override
    public Object convert(Object from,Class<?> _) {
        return Double.parseDouble((String) from);
    }
}
