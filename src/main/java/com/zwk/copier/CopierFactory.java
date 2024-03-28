package com.zwk.copier;

import com.zwk.utils.ClassUtil;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IClassBodyEvaluator;
import org.codehaus.commons.compiler.ICompilerFactory;
import org.codehaus.janino.ClassBodyEvaluator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/26 14:02
 */
@SuppressWarnings("all")
public class CopierFactory {
    private CopyParser parser;

    private Map<String, Copier<?, ?>> map = new HashMap<>();

    private static ICompilerFactory cf;

    /**
     * 创建默认CopierFactory
     *
     * @return com.zwk.copier.CopierFactory
     * @throws
     * @author zwk
     * @date 2024/3/28 10:57
     */
    public static CopierFactory createDefault() {
        return new CopierFactory();
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
        return new CopierFactory(new File(file));
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
        return new CopierFactory(file);
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
    public static CopierFactory fromInputStream(InputStream inputStream) throws FileNotFoundException, ParseException {
        return new CopierFactory(inputStream);
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
    public static CopierFactory fromReader(Reader reader) throws FileNotFoundException, ParseException {
        return new CopierFactory(reader);
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
    public static CopierFactory fromString(String s) throws FileNotFoundException, ParseException {
        return new CopierFactory(new StringReader(s));
    }

    private CopierFactory(File configFile) throws FileNotFoundException, ParseException {
        this(new FileReader(configFile));
    }

    private CopierFactory(InputStream configInPutStream) throws FileNotFoundException, ParseException {
        this(new CopyParser(configInPutStream));
    }

    private CopierFactory(Reader reader) throws ParseException {
        this(new CopyParser(reader));
    }

    private CopierFactory(CopyParser parser) throws ParseException {
        parser.root();
        this.parser = parser;
    }

    private CopierFactory() {

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
            Expression expression = Expression.createExpression(left, right);
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
                .append(expression.generateExpression(left, right))
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


    static {
        try {
            cf = CompilerFactoryFactory.getDefaultCompilerFactory(Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            cf = null;
        }
    }

}
