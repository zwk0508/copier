package com.zwk.converter;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:28
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class StringToEnumConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && Enum.class.isAssignableFrom(to);
    }

    @Override
    public Object convert(Object from, Class<?> enumClass) {
        return Enum.valueOf((Class) enumClass, (String) from);
    }
}
