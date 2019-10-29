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

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility class to work with {@link Instance}s.
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
     * @return value path as array or empty array if the input is null
     */
    public static String[] parseValuePath(@Nullable String path) {
        if (path == null)
            return new String[0];

        if (path.startsWith("+"))
            return new String[] { path };

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

        return elements.toArray(new String[0]);
    }

    /**
     * Converts an array of identifiers to a dot-separated string, enclosing identifiers, containing dots, in square
     * brackets.
     * @param path value path as array
     * @return value path as string or empty string if the input is null
     */
    public static String formatValuePath(String[] path) {
        if (path == null) {
            return "";
        }

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
     *
     * @param instance     instance
     * @param propertyPath attribute path
     * @return attribute value
     */
    public static <T> T getValueEx(Instance instance, String propertyPath) {
        String[] properties = parseValuePath(propertyPath);
        return getValueEx(instance, properties);
    }

    /**
     * Get value of an attribute according to the rules described in {@link Instance#getValueEx(String)}.
     *
     * @param instance     instance
     * @param propertyPath attribute path
     * @return attribute value
     */
    public static <T> T getValueEx(Instance instance, Instance.BeanPropertyPath propertyPath) {
        if (propertyPath.isDirectProperty()) {
            return instance.getValue(propertyPath.getFirstPropertyName());
        }

        String[] properties = propertyPath.getPropertyNames();
        return getValueEx(instance, properties);
    }

    /**
     * Get value of an attribute according to the rules described in {@link Instance#getValueEx(String)}.
     * @param instance      instance
     * @param properties    path to the attribute
     * @return              attribute value
     */
    public static <T> T getValueEx(Instance instance, String[] properties) {
        if (properties == null) {
            return null;
        }

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
        //noinspection unchecked
        return (T) currentValue;
    }

    /**
     * Set value of an attribute according to the rules described in {@link Instance#setValueEx(String, Object)}.
     *
     * @param instance     instance
     * @param propertyPath path to the attribute
     * @param value        attribute value
     */
    public static void setValueEx(Instance instance, String propertyPath, Object value) {
        String[] properties = parseValuePath(propertyPath);
        setValueEx(instance, properties, value);
    }

    /**
     * Set value of an attribute according to the rules described in {@link Instance#setValueEx(String, Object)}.
     *
     * @param instance     instance
     * @param propertyPath path to the attribute
     * @param value        attribute value
     */
    public static void setValueEx(Instance instance, Instance.BeanPropertyPath propertyPath, Object value) {
        if (propertyPath.isDirectProperty()) {
            instance.setValue(propertyPath.getFirstPropertyName(), value);
        } else {
            String[] properties = propertyPath.getPropertyNames();
            setValueEx(instance, properties, value);
        }
    }

    /**
     * Set value of an attribute according to the rules described in {@link Instance#setValueEx(String, Object)}.
     *
     * @param instance   instance
     * @param properties path to the attribute
     * @param value      attribute value
     */
    public static void setValueEx(Instance instance, String[] properties, Object value) {
        if (properties.length > 1) {

            if (properties.length == 2) {
                instance = instance.getValue(properties[0]);
            } else {
                String[] subarray = ArrayUtils.subarray(properties, 0, properties.length - 1);
                String intermediatePath = formatValuePath(subarray);
                instance = instance.getValueEx(intermediatePath);
            }

            if (instance != null) {
                String property = properties[properties.length - 1];
                instance.setValue(property, value);
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
     * @deprecated Use {@link MetadataTools#getInstanceName(com.haulmont.chile.core.model.Instance)} instead.
     * @return Instance name as defined by {@link com.haulmont.chile.core.annotations.NamePattern}
     * or <code>toString()</code>.
     * @param instance  instance
     */
    @Deprecated
    public static String getInstanceName(Instance instance) {
        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME, MetadataTools.class);
        return metadataTools.getInstanceName(instance);
    }

    /**
     * Used by {@link AbstractInstance} to check whether a property value has been changed.
     *
     * @param a an object
     * @param b  an object
     * @return true if {@code a} equals to {@code b}, but in case of {@code a} is {@link AbstractInstance} or {@code Collection} returns
     *  true only if {@code a} is the same instance as {@code b}
     */
    public static boolean propertyValueEquals(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a instanceof AbstractInstance || a instanceof Collection) {
            return false;
        }
        return a != null && a.equals(b);
    }
}