/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 15.12.2008 15:56:09
 * $Id: InstanceUtils.java 2907 2010-10-20 06:44:14Z krivopustov $
 */
package com.haulmont.chile.core.model.utils;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class InstanceUtils {

    public static String[] parseValuePath(String path) {
        List<String> elements = new ArrayList<String>();

        int breaketCount = 0;

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '[')
                breaketCount++;
            if (c == ']')
                breaketCount--;

            if ('.' != c || breaketCount > 0)
                buffer.append(c);

            if ('.' == c && breaketCount == 0) {
                String element = buffer.toString();
                if (element != null && !"".equals(element)) {
                    elements.add(element);
                } else {
                    throw new IllegalStateException("Wrong value path format");
                }
                buffer = new StringBuilder();
            }
        }
        elements.add(buffer.toString());

        return elements.toArray(new String[]{});
    }

    public static String formatValuePath(String[] path) {
        StringBuffer buffer = new StringBuffer();
        int i = 1;
        for (String s : path) {
            if (s.contains(".")){
                buffer.append("[").append(s).append("]");
            } else {
                buffer.append(s);
            }
            if (i < path.length) buffer.append(".");
            i++;
        }

        return buffer.toString();
    }

    public static <T> T getValueEx(Instance instance, String propertyPath) {
        String[] properties = parseValuePath(propertyPath);
        //noinspection unchecked
        return (T)getValueEx(instance, properties);
    }

    public static <T> T getValueEx(Instance instance, String[] properties) {
        Object currentValue = null;
        Instance currentInstance = instance;
        for (String property : properties) {
            if (currentInstance == null) break;
            currentValue = currentInstance.getValue(property);
            if (currentValue == null) break;

            currentInstance =
                currentValue instanceof Instance ?
                    (Instance)currentValue : null;
        }

        return (T) currentValue;
    }

    public static void setValueEx(Instance instance, String propertyPath, Object value) {
        String[] properties = parseValuePath(propertyPath);
        setValueEx(instance, properties, value);
    }

    public static void setValueEx(Instance instance, String[] properties, Object value) {
        if (properties.length > 1) {
            final List<String> path = Arrays.asList(properties).subList(0, properties.length - 1);
            final String __propertyPath = formatValuePath(path.toArray(new String[properties.length - 1]));

            instance = instance.getValue(__propertyPath);

            if (instance != null) {
                final String property = properties[properties.length - 1];
                instance.setValue(property, value);
            } else {
                throw new IllegalStateException(String.format("Can't find property '%s' value", __propertyPath));
            }
        } else {
            instance.setValue(properties[0], value);
        }
    }

    public static Instance copy(Instance source) {
        Instance dest;
        try {
            dest = source.getClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copy(source, dest);
        return dest;
    }

    public static void copy(Instance source, Instance dest) {
        MetaClass srcClass = source.getMetaClass();
        MetaClass dstClass = dest.getMetaClass();
        for (MetaProperty srcProperty : srcClass.getProperties()) {
            String name = srcProperty.getName();
            MetaProperty dstProperty = dstClass.getProperty(name);
            if (dstProperty != null && !dstProperty.isReadOnly())
                dest.setValue(name, source.getValue(name));
        }
    }

    public static String getInstanceName(Instance instance) {
        NamePattern annotation = instance.getClass().getAnnotation(NamePattern.class);
        if (annotation == null) {
            return instance.toString();
        } else {
            String pattern = annotation.value();
            int pos = pattern.indexOf("|");
            if (pos < 0)
                throw new IllegalArgumentException("Invalid name pattern: " + pattern);

            String format = StringUtils.substring(pattern, 0, pos);

            if (format.startsWith("#")) {
                try {
                    Method method = instance.getClass().getMethod(format.substring(1));
                    Object result = method.invoke(instance);
                    return (String) result;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
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

            String result = String.format(format, values);
            return result;
        }
    }
}
