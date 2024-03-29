package com.zwk.converter;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 16:00
 */

public class CollectionToArrayConverter implements Converter {
    @Override
    public boolean support(Class<?> from, Class<?> to) {
        return Collection.class.isAssignableFrom(from);
    }

    @Override
    public Object convert(Object from, Class<?> to) {
        Collection collection = (Collection) from;
        if (collection == null || collection.isEmpty()) {
            return Array.newInstance(to, 0);
        } else {
            int size = collection.size();
            int i = 0;
            if (to == byte.class) {
                byte[] bytes = new byte[size];
                for (Object object : collection) {
                    bytes[i++] = (byte) object;
                }
                return bytes;
            } else if (to == short.class) {
                short[] shorts = new short[size];
                for (Object object : collection) {
                    shorts[i++] = (short) object;
                }
                return shorts;
            } else if (to == int.class) {
                int[] ints = new int[size];
                for (Object object : collection) {
                    ints[i++] = (int) object;
                }
                return ints;
            } else if (to == long.class) {
                long[] longs = new long[size];
                for (Object object : collection) {
                    longs[i++] = (long) object;
                }
                return longs;
            } else if (to == float.class) {
                float[] floats = new float[size];
                for (Object object : collection) {
                    floats[i++] = (float) object;
                }
                return floats;
            } else if (to == double.class) {
                double[] doubles = new double[size];
                for (Object object : collection) {
                    doubles[i++] = (double) object;
                }
                return doubles;
            } else if (to == char.class) {
                char[] chars = new char[size];
                for (Object object : collection) {
                    chars[i++] = (char) object;
                }
                return chars;
            } else if (to == boolean.class) {
                boolean[] booleans = new boolean[size];
                for (Object object : collection) {
                    booleans[i++] = (boolean) object;
                }
                return booleans;
            } else {
                return collection.toArray((Object[]) Array.newInstance(to, collection.size()));
            }
        }
    }
}
