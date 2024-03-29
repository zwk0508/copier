package com.zwk.converter;

import java.math.BigInteger;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:28
 */

public class StringToBigIntegerConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && BigInteger.class.equals(to);
    }

    @Override
    public Object convert(Object from, Class<?> _) {
        return new BigInteger((String) from);
    }
}
