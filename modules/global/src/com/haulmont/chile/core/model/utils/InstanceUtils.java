/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.chile.core.model.utils;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Utility class to work with {@link Instance}s.
 */
public final class InstanceUtils {

    private static final Pattern INSTANCE_NAME_SPLIT_PATTERN = Pattern.compile("[,;]");

    private InstanceUtils() {
    }

    /**
     * Converts a string of identifiers separated by dots to an array. A part of the given string, enclosed in square
     * brackets, treated as single identifier. For example:
     * <pre>
     *     car.driver.name
     *     [car.field].driver.name
     * </pre>
     * @param path value path as string
     * @return value path as array or empty array if the input is null
     */
    public static String[] parseValuePath(@Nullable String path) {
        if (path == null)
            return new String[0];

        List<String> elements = new ArrayList<>(4);

        int bracketCount = 0;

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '[')
                bracketCount++;
            if (c == ']')
                bracketCount--;

            if ('.' != c || bracketCount > 0)
                buffer.append(c);

            if ('.' == c && bracketCount == 0) {
                String element = buffer.toString();
                if (!"".equals(element)) {
                    elements.add(element);
                } else {
                    throw new IllegalStateException("Wrong value path format");
                }
                buffer = new StringBuilder();
            }
        }
        elements.add(buffer.toString());

        return elements.toArray(new String[elements.size()]);
    }

    /**
     * Converts an array of identifiers to a dot-separated string, enclosing identifiers, containing dots, in square
     * brackets.
     * @param path value path as array
     * @return value path as string or empty string if the input is null
     */
    public static String formatValuePath(String[] path) {
        if (path == null)
            return "";

        StringBuilder buffer = new StringBuilder();
        int i = 1;
        for (String s : path) {
            if (s.contains(".")) {
                buffer.append("[").append(s).append("]");
            } else {
                buffer.append(s);
            }
            if (i < path.length) buffer.append(".");
            i++;
        }
        return buffer.toString();
    }

    /**
     * Get value of an attribute according to the rules described in {@link Instance#getValueEx(String)}.
     * @param instance      instance
     * @param propertyPath  attribute path
     * @return              attribute value
     */
    public static <T> T getValueEx(Instance instance, String propertyPath) {
        String[] properties = parseValuePath(propertyPath);
        //noinspection unchecked
        return getValueEx(instance, properties);
    }

    /**
     * Get value of an attribute according to the rules described in {@link Instance#getValueEx(String)}.
     * @param instance      instance
     * @param properties    path to the attribute
     * @return              attribute value
     */
    public static <T> T getValueEx(Instance instance, String[] properties) {
        if (properties == null)
            return null;

        Object currentValue = null;
        Instance currentInstance = instance;
        for (String property : properties) {
            if (currentInstance == null)
                break;

            currentValue = currentInstance.getValue(property);
            if (currentValue == null)
                break;

            currentInstance = currentValue instanceof Instance ? (Instance) currentValue : null;
        }
        return (T) currentValue;
    }

    /**
     * Set value of an attribute according to the rules described in {@link Instance#setValueEx(String, Object)}.
     * @param instance      instance
     * @param propertyPath  path to the attribute
     * @param value         attribute value
     */
    public static void setValueEx(Instance instance, String propertyPath, Object value) {
        String[] properties = parseValuePath(propertyPath);
        setValueEx(instance, properties, value);
    }

    /**
     * Set value of an attribute according to the rules described in {@link Instance#setValueEx(String, Object)}.
     * @param instance      instance
     * @param properties    path to the attribute
     * @param value         attribute value
     */
    public static void setValueEx(Instance instance, String[] properties, Object value) {
        if (properties.length > 1) {
            String[] subarray = (String[]) ArrayUtils.subarray(properties, 0, properties.length - 1);
            String intermediatePath = formatValuePath(subarray);

            instance = instance.getValueEx(intermediatePath);

            if (instance != null) {
                String property = properties[properties.length - 1];
                instance.setValue(property, value);
            } else {
                throw new IllegalStateException(String.format("Can't find property '%s' value", intermediatePath));
            }
        } else {
            instance.setValue(properties[0], value);
        }
    }

    /**
     * Use com.haulmont.cuba.core.global.MetadataTools#copy instead
     */
    @Deprecated
    public static Instance copy(Instance source) {
        return AppBeans.get(MetadataTools.NAME, MetadataTools.class).copy(source);
    }

    /**
     * Use com.haulmont.cuba.core.global.MetadataTools#copy instead
     */
    @Deprecated
    public static void copy(Instance source, Instance dest) {
        AppBeans.get(MetadataTools.NAME, MetadataTools.class).copy(source, dest);
    }

    /**
     * @return Instance name as defined by {@link com.haulmont.chile.core.annotations.NamePattern}
     * or <code>toString()</code>.
     * @param instance  instance
     */
    public static String getInstanceName(Instance instance) {
        checkNotNullArgument(instance, "instance is null");

        NamePatternRec rec = parseNamePattern(instance.getMetaClass());
        if (rec == null) {
            return instance.toString();
        } else {
            if (rec.methodName != null) {
                try {
                    Method method = instance.getClass().getMethod(rec.methodName);
                    Object result = method.invoke(instance);
                    return (String) result;
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException("Error getting instance name", e);
                }
            }
            if (rec.isSpEL) {
                return evaluateSpEL(instance, rec.format);
            }

            // lazy initialized messages, used only for enum values
            Messages messages = null;

            Object[] values = new Object[rec.fields.length];
            for (int i = 0; i < rec.fields.length; i++) {
                Object value = instance.getValue(rec.fields[i]);
                if (value == null) {
                    values[i] = "";
                } else if (value instanceof Instance) {
                    values[i] = getInstanceName((Instance) value);
                } else if (value instanceof EnumClass) {
                    if (messages == null) {
                        messages = AppBeans.get(Messages.NAME);
                    }

                    values[i] = messages.getMessage((Enum)value);
                } else {
                    values[i] = value;
                }
            }

            return String.format(rec.format, values);
        }
    }

    public static String evaluateSpEL(Instance instance, String format) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(instance);
        BeanResolver beanResolver = new BeanFactoryResolver(AppContext.getApplicationContext());
        evaluationContext.setBeanResolver(beanResolver);
        Expression expression = parser.parseExpression(format, new TemplateParserContext());
        return expression.getValue(evaluationContext, String.class);
    }

    /**
     * Parse a name pattern defined by {@link NamePattern} annotation.
     * @param metaClass entity meta-class
     * @return record containing the name pattern properties, or null if the @NamePattern is not defined for the meta-class
     */
    @Nullable
    public static NamePatternRec parseNamePattern(MetaClass metaClass) {
        Map attributes = (Map) metaClass.getAnnotations().get(NamePattern.class.getName());
        if (attributes == null)
            return null;
        String pattern = (String) attributes.get("value");
        if (StringUtils.isBlank(pattern))
            return null;

        int pos = pattern.indexOf("|");
        if (pos < 0)
            throw new DevelopmentException("Invalid name pattern: " + pattern);

        String format = StringUtils.substring(pattern, 0, pos);
        String trimmedFormat = format.trim();
        boolean isSpEL = trimmedFormat.startsWith("#{");
        String methodName = trimmedFormat.startsWith("#") && !isSpEL ?
                trimmedFormat.substring(1) :
                null;
        String fieldsStr = StringUtils.substring(pattern, pos + 1);
        String[] fields = INSTANCE_NAME_SPLIT_PATTERN.split(fieldsStr);
        return new NamePatternRec(format, methodName, fields, isSpEL);
    }

    public static class NamePatternRec {
        /**
         * Name pattern string format
         */
        public final String format;
        /**
         * Formatting method name or null
         */
        public final String methodName;
        /**
         * Array of property names
         */
        public final String[] fields;
        /**
         * Is name pattern described as SpEL
         */
        public final boolean isSpEL;

        public NamePatternRec(String format, String methodName, String[] fields, boolean isSpEL) {
            this.fields = fields;
            this.format = format;
            this.methodName = methodName;
            this.isSpEL = isSpEL;
        }
    }

    /**
     * Used by {@link AbstractInstance} to check whether property value has been changed.
     *
     * @param a an object
     * @param b  an object
     * @return true if a equals to b, but in case of a is {@link AbstractInstance} returns true only if a are the same instance as b
     */
    public static boolean propertyValueEquals(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a instanceof AbstractInstance) {
            return false;
        }
        return a != null && a.equals(b);
    }
}