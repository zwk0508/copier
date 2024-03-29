package com.zwk.converter;

import java.math.BigDecimal;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:28
 */

public class StringToBigDecimalConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return String.class.equals(from) && BigDecimal.class.equals(to);
    }

    @Override
    public Object convert(Object from, Class<?> _) {
        return new BigDecimal((String) from);
    }
}
