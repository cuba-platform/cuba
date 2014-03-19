/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.chile.core.model.utils;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.IllegalEntityStateException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to work with {@link Instance}s.
 *
 * @author abramov
 * @version $Id$
 */
public final class InstanceUtils {

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
     * @return value path as array
     */
    public static String[] parseValuePath(String path) {
        List<String> elements = new ArrayList<>();

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
     * @return value path as string
     */
    public static String formatValuePath(String[] path) {
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
     * Create a new instance and make it a shallow copy of the instance given.
     * <p/> This method copies attributes according to the metadata and relies on {@link com.haulmont.chile.core.model.Instance#getMetaClass()}
     * method which should not return null.
     * @param source    source instance
     * @return          new instance of the same Java class as source
     */
    public static Instance copy(Instance source) {
        Preconditions.checkNotNullArgument(source, "source is null");

        Instance dest;
        try {
            dest = source.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copy(source, dest);
        return dest;
    }

    /**
     * Make a shallow copy of an instance.
     * <p/> This method copies attributes according to the metadata and relies on {@link com.haulmont.chile.core.model.Instance#getMetaClass()}
     * method which should not return null for both objects.
     * <p/> The source and destination instances don't have to be of the same Java class or metaclass. Copying is
     * performed in the following scenario: get each source property and copy the value to the destination if it
     * contains a property with the same name and it is not read-only.
     * @param source    source instance
     * @param dest      destination instance
     */
    public static void copy(Instance source, Instance dest) {
        Preconditions.checkNotNullArgument(source, "source is null");
        Preconditions.checkNotNullArgument(dest, "dest is null");

        for (MetaProperty srcProperty : source.getMetaClass().getProperties()) {
            String name = srcProperty.getName();
            MetaProperty dstProperty = dest.getMetaClass().getProperty(name);
            if (dstProperty != null && !dstProperty.isReadOnly()) {
                try {
                    dest.setValue(name, source.getValue(name));
                } catch (RuntimeException e) {
                    Throwable cause = ExceptionUtils.getRootCause(e);
                    if (cause == null)
                        cause = e;
                    // ignore exception on copy for not loaded fields
                    if (!(cause instanceof IllegalEntityStateException))
                        throw e;
                }
            }
        }
    }

    /**
     * @return Instance name as defined by {@link com.haulmont.chile.core.annotations.NamePattern}
     * or <code>toString()</code>.
     * @param instance  instance
     */
    public static String getInstanceName(Instance instance) {
        Preconditions.checkNotNullArgument(instance, "instance is null");

        String pattern = (String) instance.getMetaClass().getAnnotations().get(NamePattern.class.getName());
        if (StringUtils.isBlank(pattern)) {
            return instance.toString();
        } else {
            int pos = pattern.indexOf("|");
            if (pos < 0)
                throw new IllegalArgumentException("Invalid name pattern: " + pattern);

            String format = StringUtils.substring(pattern, 0, pos);

            if (format.startsWith("#")) {
                try {
                    Method method = instance.getClass().getMethod(format.substring(1));
                    Object result = method.invoke(instance);
                    return (String) result;
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            String fieldsStr = StringUtils.substring(pattern, pos + 1);

            String[] fields = fieldsStr.split("[,;]");
            Object[] values = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                Object value = instance.getValue(fields[i]);
                if (value == null) {
                    values[i] = "";
                } else if (value instanceof Instance)
                    values[i] = getInstanceName((Instance) value);
                else
                    values[i] = value;
            }

            return String.format(format, values);
        }
    }
}