package com.zwk.converter;

import java.util.*;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 15:54
 */

public class ArrayToCollectionConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return from.isArray() && Collection.class.isAssignableFrom(to);
    }


    @Override
    public Object convert(Object from, Class<?> to) {
        Collection list;
        if (Set.class.isAssignableFrom(to)) {
            list = new HashSet();
        }else {
            list = new ArrayList();
        }
        if (from == null) {
            return list;
        }
        Class<?> componentType = from.getClass().getComponentType();
        if (componentType == byte.class) {
            for (byte b : (byte[]) from) {
                list.add(b);
            }
        } else if (componentType == short.class) {
            for (short i : (short[]) from) {
                list.add(i);
            }
        } else if (componentType == int.class) {
            for (int i : (int[]) from) {
                list.add(i);
            }
        } else if (componentType == long.class) {
            for (long l : (long[]) from) {
                list.add(l);
            }
        } else if (componentType == float.class) {
            for (float v : (float[]) from) {
                list.add(v);
            }
        } else if (componentType == double.class) {
            for (double v : (double[]) from) {
                list.add(v);
            }
        } else if (componentType == char.class) {
            for (char c : (char[]) from) {
                list.add(c);
            }
        } else if (componentType == boolean.class) {
            for (boolean b : (boolean[]) from) {
                list.add(b);
            }
        } else {
            list.addAll(Arrays.asList((Object[]) from));
        }
        return list;
    }
}
