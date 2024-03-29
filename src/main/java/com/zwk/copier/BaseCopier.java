package com.zwk.copier;

import com.zwk.converter.Converter;

import java.util.List;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 17:04
 */

public class BaseCopier {
    private CopierFactory factory;

    public void setFactory(CopierFactory factory) {
        this.factory = factory;
    }

    public boolean canConvert(Class<?> from, Class<?> to) {
        return support(factory.getCustomConverters(), from, to) || support(factory.getDefaultConverters(), from, to);
    }

    private boolean support(List<Converter> converters, Class<?> from, Class<?> to) {
        for (Converter converter : converters) {
            if (converter.support(from, to)) {
                return true;
            }
        }
        return false;
    }

}
