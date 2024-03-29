package com.zwk.converter;

public interface Converter {
    boolean support(Class<?> from, Class<?> to);

    Object convert(Object from, Class<?> to);

}
