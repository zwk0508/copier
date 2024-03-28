package com.zwk.utils;

import java.util.Map;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/28 11:22
 */

public class ClassUtil {
    public static boolean isMap(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }
}
