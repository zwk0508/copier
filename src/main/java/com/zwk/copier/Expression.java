package com.zwk.copier;

import com.zwk.utils.ClassUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/26 18:14
 */

public class Expression {

    public static final String SOURCE_PARAM_NAME = "p0";
    public static final String TARGET_PARAM_NAME = "p1";
    private List<Pair> pairs = new ArrayList<>();


    public void addPair(Pair pair) {
        pairs.add(pair);
    }

    public String generateExpression(Class<?> left, Class<?> right, boolean useConverter) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean leftMap = ClassUtil.isMap(left);
        boolean rightMap = ClassUtil.isMap(right);

        Map<String, PropertyDescriptor> leftProperty = null;
        if (!leftMap) {
            leftProperty = getPropertyDescriptors(left);
        }
        Map<String, PropertyDescriptor> rightProperty = null;
        if (!rightMap) {
            rightProperty = getPropertyDescriptors(right);
        }

        for (Pair pair : pairs) {
            sb.append(SOURCE_PARAM_NAME)
                    .append(".");
            if (leftMap) {
                sb.append("put(\"")
                        .append(pair.getLeft())
                        .append("\",");
            } else {
                Class<?> leftClass = pair.getLeftClass();
                if (leftClass == null) {
                    PropertyDescriptor propertyDescriptor = leftProperty.get(pair.getLeft());
                    if (propertyDescriptor == null || propertyDescriptor.getWriteMethod() == null) {
                        throw new NoSuchMethodException(left.getName() + " no set method for property: " + pair.getLeft());
                    }
                    leftClass = propertyDescriptor.getWriteMethod().getParameterTypes()[0];
                    pair.setLeftClass(leftClass);
                }
                sb.append("set")
                        .append(toTitle(pair.getLeft()))
                        .append("(");
            }
            if (rightMap) {
                if (!leftMap) {
                    Class<?> leftClass = pair.getLeftClass();
                    sb.append("(")
                            .append(leftClass.getName())
                            .append(")");
                }
                sb.append(TARGET_PARAM_NAME).append(".");
                sb.append("get(\"")
                        .append(pair.getRight())
                        .append("\")");
            } else {
                Class<?> rightClass = pair.getRightClass();
                if (rightClass == null) {
                    PropertyDescriptor propertyDescriptor = rightProperty.get(pair.getRight());
                    if (propertyDescriptor == null || propertyDescriptor.getReadMethod() == null) {
                        throw new NoSuchMethodException(right.getName() + " no get method for property: " + pair.getRight());
                    }
                    rightClass = propertyDescriptor.getReadMethod().getReturnType();

                    if (!leftMap) {
                        if (!pair.getLeftClass().isAssignableFrom(rightClass)) {
                            throw new IllegalArgumentException("property " + pair.getLeft() + " class is not compatible with right property class");
                        }
                    }
                    pair.setRightClass(rightClass);
                }

                sb.append(TARGET_PARAM_NAME).append(".");
                sb.append("get")
                        .append(toTitle(pair.getRight()))
                        .append("()");
            }
            sb.append(");\n");

        }
        return sb.toString();
    }

    private static String toTitle(String s) {
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static Expression createExpression(Class<?> left, Class<?> right, boolean userConverter) throws IntrospectionException {
        boolean leftMap = ClassUtil.isMap(left);
        boolean rightMap = ClassUtil.isMap(right);
        if (leftMap && rightMap) {
            throw new IllegalArgumentException("both left and right are Map");
        }
        Expression expression = new Expression();
        if (!leftMap && !rightMap) {
            Map<String, PropertyDescriptor> leftProperty = getPropertyDescriptors(left);
            Map<String, PropertyDescriptor> rightProperty = getPropertyDescriptors(right);

            for (Map.Entry<String, PropertyDescriptor> entry : leftProperty.entrySet()) {
                String key = entry.getKey();
                PropertyDescriptor rightPropertyDescriptor = rightProperty.get(key);
                if (rightPropertyDescriptor == null) {
                    continue;
                }
                PropertyDescriptor leftPropertyDescriptor = entry.getValue();
                Method writeMethod = leftPropertyDescriptor.getWriteMethod();
                if (writeMethod == null) {
                    continue;
                }

                Method readMethod = rightPropertyDescriptor.getReadMethod();
                if (readMethod == null) {
                    continue;
                }

                Class<?> parameterType = writeMethod.getParameterTypes()[0];

                Class<?> returnType = readMethod.getReturnType();

                if (parameterType.isAssignableFrom(returnType)) {
                    Pair pair = new Pair();
                    pair.setLeft(key);
                    pair.setRight(key);
                    pair.setLeftClass(parameterType);
                    pair.setRightClass(returnType);
                    expression.addPair(pair);
                }
            }

        } else if (leftMap) {
            Map<String, PropertyDescriptor> rightProperty = getPropertyDescriptors(right);

            for (Map.Entry<String, PropertyDescriptor> entry : rightProperty.entrySet()) {
                String key = entry.getKey();
                if (Objects.equals("class", key)) {
                    continue;
                }
                PropertyDescriptor propertyDescriptor = entry.getValue();
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod == null) {
                    continue;
                }
                Pair pair = new Pair();
                pair.setLeft(key);
                pair.setRight(key);
                expression.addPair(pair);
            }
        } else {
            Map<String, PropertyDescriptor> leftProperty = getPropertyDescriptors(left);
            for (Map.Entry<String, PropertyDescriptor> entry : leftProperty.entrySet()) {
                String key = entry.getKey();
                PropertyDescriptor propertyDescriptor = entry.getValue();
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (writeMethod == null) {
                    continue;
                }
                Pair pair = new Pair();
                pair.setLeft(key);
                pair.setRight(key);
                pair.setLeftClass(writeMethod.getParameterTypes()[0]);
                expression.addPair(pair);
            }
        }
        return expression;
    }

    private static Map<String, PropertyDescriptor> getPropertyDescriptors(Class<?> clazz) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        if (propertyDescriptors == null || propertyDescriptors.length == 0) {
            return Collections.emptyMap();
        }
        Map<String, PropertyDescriptor> map = new HashMap<>();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            map.put(propertyDescriptor.getName(), propertyDescriptor);
        }
        return map;
    }
}
