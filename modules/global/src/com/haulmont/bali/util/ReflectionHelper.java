/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.02.2009 11:00:46
 *
 * $Id: ReflectionHelper.java 3028 2010-11-09 08:12:36Z krivopustov $
 */
package com.haulmont.bali.util;

import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.*;

public class ReflectionHelper
{
    public static <T> Class<T> getClass(String name) {
        try {
            return (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> cls, Object... params) throws NoSuchMethodException {
        Class[] paramTypes = getParamTypes(params);

        Constructor<T> constructor = cls.getConstructor(paramTypes);
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method findMethod(Class c, String name, Object...params) {
        Class[] paramTypes = getParamTypes(params);

        Method method = null;
        try {
            method = c.getDeclaredMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            try {
                method = c.getMethod(name, paramTypes);
            } catch (NoSuchMethodException e1) {
                //
            }
        }
        if (method != null)
            method.setAccessible(true);

        return method;
    }

    public static <T> T invokeMethod(Object obj, String name, Object...params) throws NoSuchMethodException
    {
        Class[] paramTypes = getParamTypes(params);

        final Class<?> aClass = obj.getClass();
        Method method;
        try {
            method = aClass.getDeclaredMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            method = aClass.getMethod(name, paramTypes);
        }
        method.setAccessible(true);
        try {
            //noinspection unchecked
            return (T) method.invoke(obj, params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class[] getParamTypes(Object... params) {
        List<Class> paramClasses = new ArrayList<Class>();
        for (Object param : params) {
            if (param == null)
                throw new IllegalStateException("Null parameter");

            final Class aClass = param.getClass();
            if (List.class.isAssignableFrom(aClass)) {
                paramClasses.add(List.class);
            } else if (Set.class.isAssignableFrom(aClass)) {
                paramClasses.add(Set.class);
            } else if (Map.class.isAssignableFrom(aClass)) {
                paramClasses.add(Map.class);
            } else if (Element.class.isAssignableFrom(aClass)) {
                paramClasses.add(Element.class);
            } else {
                paramClasses.add(aClass);
            }
        }
        return paramClasses.toArray(new Class<?>[paramClasses.size()]);
    }
}
