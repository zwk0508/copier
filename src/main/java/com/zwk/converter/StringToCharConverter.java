package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:28
 */

public class StringToCharConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && (Character.class.equals(to)|| char.class.equals(to));
    }

    @Override
    public Object convert(Object from, Class<?> _) {
        String s = (String) from;
        if (s == null || s.isEmpty()) {
            return '\0';
        }
        return s.charAt(0);
    }
}
