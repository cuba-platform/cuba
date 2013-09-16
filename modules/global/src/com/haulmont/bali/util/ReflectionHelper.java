/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.bali.util;

import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Utility class to simplify work with Java reflection.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ReflectionHelper {

    /**
     * Load class by name.
     * @param name  class FQN
     * @return      class instance
     * @throws ClassNotFoundException if not found
     */
    public static Class<?> loadClass(String name) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(name);
    }

    /**
     * Load class by name, wrapping a {@link ClassNotFoundException} into unchecked exception.
     * @param name  class FQN
     * @return      class instance
     */
    public static <T> Class<T> getClass(String name) {
        try {
            return (Class<T>) loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Instantiates an object by appropriate constructor.
     * @param cls       class
     * @param params    constructor arguments
     * @return          created object instance
     * @throws NoSuchMethodException    if the class has no constructor matching the given arguments
     */
    public static <T> T newInstance(Class<T> cls, Object... params) throws NoSuchMethodException {
        Class[] paramTypes = getParamTypes(params);

        Constructor<T> constructor = cls.getConstructor(paramTypes);
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches for a method by its name and arguments.
     * @param c         class
     * @param name      method name
     * @param params    method arguments
     * @return          method reference or null if a suitable method not found
     */
    @Nullable
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

    /**
     * Invokes a method by reflection.
     * @param obj       object instance
     * @param name      method name
     * @param params    method arguments
     * @return          method result
     * @throws NoSuchMethodException if a suitable method not found
     */
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
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs an array of argument types from an array of actual values. Values can not contain nulls.
     * @param params    arguments
     * @return          the array of argument types
     */
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
