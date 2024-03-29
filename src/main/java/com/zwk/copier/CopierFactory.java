package com.zwk.copier;

import com.zwk.converter.*;
import com.zwk.utils.ClassUtil;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IClassBodyEvaluator;
import org.codehaus.commons.compiler.ICompilerFactory;
import org.codehaus.janino.ClassBodyEvaluator;

import java.io.*;
import java.util.*;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/26 14:02
 */
@SuppressWarnings("all")
public class CopierFactory {
    private CopyParser parser;

    private Map<String, Copier<?, ?>> map = new HashMap<>();

    private List<Converter> defaultConverters = new ArrayList<>();
    private List<Converter> customConverters = new ArrayList<>();

    private final boolean useConverter;

    private static ICompilerFactory cf;

    /**
     * 创建默认CopierFactory
     *
     * @param useConverter 是否使用转换器
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory createDefault(boolean useConverter) {
        return new CopierFactory(useConverter);
    }

    /**
     * 创建默认CopierFactory
     *
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory createDefault() {
        return new CopierFactory(false);
    }

    /**
     * 从文件读取配置
     *
     * @param file
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory fromFile(String file) throws FileNotFoundException, ParseException {
        return fromFile(file, false);
    }

    /**
     * 从文件读取配置
     *
     * @param file
     * @param useConverter 是否使用转换器
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory fromFile(String file, boolean useConverter) throws FileNotFoundException, ParseException {
        return fromReader(new FileReader(file), useConverter);
    }

    /**
     * 从文件读取配置
     *
     * @param file
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory fromFile(File file) throws FileNotFoundException, ParseException {
        return fromFile(file, false);
    }

    /**
     * 从文件读取配置
     *
     * @param file
     * @param useConverter 是否使用转换器
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory fromFile(File file, boolean useConverter) throws FileNotFoundException, ParseException {
        return fromReader(new FileReader(file), useConverter);
    }


    /**
     * 从InputStream读取配置
     *
     * @param inputStream
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory fromInputStream(InputStream inputStream, boolean useConverter) throws ParseException {
        return new CopierFactory(inputStream, useConverter);
    }

    /**
     * 从InputStream读取配置
     *
     * @param inputStream
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory fromInputStream(InputStream inputStream) throws ParseException {
        return fromInputStream(inputStream, false);
    }

    /**
     * 从Reader读取配置
     *
     * @param reader
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory fromReader(Reader reader, boolean useConverter) throws ParseException {
        return new CopierFactory(reader, useConverter);
    }

    /**
     * 从String读取配置
     *
     * @param s
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory fromString(String s) throws ParseException {
        return fromReader(new StringReader(s), false);
    }

    /**
     * 从String读取配置
     *
     * @param s
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory fromString(String s, boolean useConverter) throws ParseException {
        return fromReader(new StringReader(s), useConverter);
    }

    private CopierFactory(InputStream configInPutStream, boolean useConverter) throws ParseException {
        this(new CopyParser(configInPutStream), useConverter);
    }

    private CopierFactory(Reader reader, boolean useConverter) throws ParseException {
        this(new CopyParser(reader), useConverter);
    }

    private CopierFactory(CopyParser parser, boolean useConverter) throws ParseException {
        parser.root();
        this.parser = parser;
        this.useConverter = useConverter;
    }

    private CopierFactory(boolean useConverter) {
        this.useConverter = false;
    }

    {
        defaultConverters.add(new StringToByteConverter());
        defaultConverters.add(new StringToShortConverter());
        defaultConverters.add(new StringToIntegerConverter());
        defaultConverters.add(new StringToLongConverter());
        defaultConverters.add(new StringToFloatConverter());
        defaultConverters.add(new StringToDoubleConverter());
        defaultConverters.add(new StringToBooleanConverter());
        defaultConverters.add(new StringToCharConverter());
        defaultConverters.add(new StringToBigIntegerConverter());
        defaultConverters.add(new StringToBigDecimalConverter());
        defaultConverters.add(new StringToEnumConverter());
        defaultConverters.add(new EnumToStringConverter());
        defaultConverters.add(new ArrayToCollectionConverter());
        defaultConverters.add(new CollectionToArrayConverter());

        defaultConverters.add(new ObjectToStringConverter());
    }

    public void registerConverter(Converter converter) {
        customConverters.add(converter);
    }


    public <L, R> Copier<L, R> createCopier(Class<L> left, Class<R> right) throws Exception {
        Objects.requireNonNull(left, "left was null");
        Objects.requireNonNull(right, "right was null");

        String sourceName = left.getName();
        String targetName = right.getName();
        String key = sourceName + targetName;
        Copier<?, ?> copier = map.get(key);
        if (copier != null) {
            return (Copier<L, R>) copier;
        }
        synchronized (this) {
            copier = map.get(key);
            if (copier != null) {
                return (Copier<L, R>) copier;
            }

            boolean b = ClassUtil.isMap(left) && ClassUtil.isMap(right);
            if (b) {
                Copier mapCopy = new Copier<Map, Map>() {
                    @Override
                    public void copy(Map left, Map right) {
                        left.putAll(right);
                    }
                };
                map.put(key, mapCopy);
                return mapCopy;
            }
            Expression expression = Expression.createExpression(left, right, useConverter);
            return createCopier(left, right, sourceName, targetName, expression, key);
        }
    }


    public <L, R> Copier<L, R> createCopier(String id, Class<L> left, Class<R> right) throws Exception {
        Objects.requireNonNull(parser, "CopierFactory parser was null");
        Objects.requireNonNull(id, "id was null");
        Objects.requireNonNull(left, "left was null");
        Objects.requireNonNull(right, "right was null");

        String sourceName = left.getName();
        String targetName = right.getName();
        String key = id + sourceName + targetName;
        Copier<?, ?> copier = map.get(key);
        if (copier != null) {
            return (Copier<L, R>) copier;
        }
        synchronized (this) {
            copier = map.get(key);
            if (copier != null) {
                return (Copier<L, R>) copier;
            }
            Expression expression = parser.getExpression(id);
            if (expression == null) {
                throw new IllegalArgumentException("unknown id : " + id);
            }
            return createCopier(left, right, sourceName, targetName, expression, key);
        }
    }

    private <L, R> Copier<L, R> createCopier(Class<L> left, Class<R> right, String sourceName, String targetName, Expression expression, String key) throws Exception {
        IClassBodyEvaluator cbe;
        if (cf == null) {
            cbe = new ClassBodyEvaluator();
        } else {
            cbe = cf.newClassBodyEvaluator();
        }

        cbe.setExtendedClass(BaseCopier.class);
        cbe.setDefaultImports(sourceName, targetName);
        cbe.setImplementedInterfaces(new Class[]{Copier.class});
        String ssName = left.getSimpleName();
        String tsName = right.getSimpleName();
        StringBuilder sb = new StringBuilder();
        String leftParamName = Expression.SOURCE_PARAM_NAME;
        String rightParamName = Expression.TARGET_PARAM_NAME;
        sb.append("public void copy(")
                .append(ssName)
                .append(" ")
                .append(leftParamName)
                .append(",")
                .append(tsName)
                .append(" ")
                .append(rightParamName)
                .append("){")
                .append(expression.generateExpression(left, right, useConverter))
                .append("}")
                .append("public void copy(")
                .append("Object ")
                .append(leftParamName)
                .append(",")
                .append("Object ")
                .append(rightParamName)
                .append("){copy((")
                .append(ssName)
                .append(")")
                .append(leftParamName)
                .append(",(")
                .append(tsName)
                .append(")")
                .append(rightParamName)
                .append(");}");
        cbe.cook(sb.toString());
        Object object = cbe.getClazz().newInstance();
        map.put(key, (Copier<?, ?>) object);
        return (Copier<L, R>) object;
    }

    public List<Converter> getDefaultConverters() {
        return defaultConverters;
    }

    public List<Converter> getCustomConverters() {
        return customConverters;
    }

    static {
        try {
            cf = CompilerFactoryFactory.getDefaultCompilerFactory(Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            cf = null;
        }
    }

}
