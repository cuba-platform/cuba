/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx.entity;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularType;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author budarov
 * @version $Id$
 */
public class AttributeHelper {

    public static Object convert(String type, String str) {
        if (str == null)
            return null;

        if (String.class.getName().equals(type)) {
            return str;
        } else if ("int".equals(type) || Integer.class.getName().equals(type)) {
            return Integer.valueOf(str);
        } else if ("long".equals(type) || Long.class.getName().equals(type)) {
            return Long.valueOf(str);
        } else if ("double".equals(type) || Double.class.getName().equals(type)) {
            return Double.valueOf(str);
        } else if ("float".equals(type) || Float.class.getName().equals(type)) {
            return Float.valueOf(str);
        } else if ("boolean".equals(type) || Boolean.class.getName().equals(type)) {
            return Boolean.valueOf(str);
        } else if (ObjectName.class.getName().equals(type)) {
            try {
                return new ObjectName(str);
            } catch (MalformedObjectNameException e) {
                throw new IllegalArgumentException(e);
            }
        } else if (UUID.class.getName().equals(type)) {
            return UUID.fromString(str);
        }
        return null;
    }

    public static boolean isBoolean(String type) {
        return "boolean".equals(type) || Boolean.class.getName().equals(type);
    }

    public static boolean isArray(String type) {
        return type != null && (type.startsWith("[L") || type.endsWith("[]"));
    }

    public static String convertToString(Object value) {
        if (value == null) {
            return null;
        }

        if (value.getClass().isArray()) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < Array.getLength(value); i++) {
                Object o = Array.get(value, i);
                b.append(convertToString(o)).append("\n");
            }
            return b.toString();
        }
        else if (value instanceof CompositeData) {
            return compositeToString((CompositeData) value);
        }
        else if (value instanceof TabularData) {
            return tabularToString((TabularData) value);
        }
        return value.toString();
    }

    private static String tabularToString(TabularData tabularData) {
        TabularType type = tabularData.getTabularType();
        StringBuilder b = new StringBuilder();
        b.append("(").append(type.getTypeName()).append(")\n");
        Collection<CompositeData> values = (Collection) tabularData.values();
        for (CompositeData cd: values) {
            b.append(compositeToString(cd));
        }
        return b.toString();
    }

    private static String compositeToString(CompositeData compositeData) {
        if (canConvertToTrueObject(compositeData)) {
            try {
                Object trueObject = convertToTrueObject(compositeData);
                return String.valueOf(trueObject);
            }
            catch (Exception e) {
                return e.getClass().getName() + " " + e.getMessage();
            }
        }

        CompositeType type = compositeData.getCompositeType();

        StringBuilder b = new StringBuilder();
        b.append("[");
        List<String> keys = new ArrayList<>(type.keySet());
        Collections.sort(keys); // alphabetically
        for (String key: keys) {
            b.append(key).append(": ");
            Object value = compositeData.get(key);
            b.append(convertToString(value));
            if (keys.indexOf(key) != keys.size() - 1) {
                b.append(", ");
            }
        }
        b.append("]\n");
        return b.toString();
    }

    /*
     * Try to find factory method with signature like
     * public static VMOption from(CompositeData compositeData) { ... }
     */
    private static Object convertToTrueObject(CompositeData compositeData) {
        CompositeType type = compositeData.getCompositeType();
        try {
            Class _class = Class.forName(type.getTypeName());
            Method method = _class.getMethod("from", CompositeData.class);
            if (Modifier.isStatic(method.getModifiers()) && method.getReturnType() == _class) {
                return method.invoke(null, compositeData);
            }
            return null;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean canConvertToTrueObject(CompositeData compositeData) {
        CompositeType type = compositeData.getCompositeType();
        try {
            Class _class = Class.forName(type.getTypeName());
            Method method = _class.getMethod("from", CompositeData.class);
            if (Modifier.isStatic(method.getModifiers())
                    && Modifier.isPublic(method.getModifiers())
                    && method.getReturnType() == _class) {
                return true;
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }

        return false;
    }
}