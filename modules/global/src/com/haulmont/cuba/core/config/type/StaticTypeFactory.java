/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config.type;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * A factory that builds a type by invoking a static method, supplying
 * the string value as a parameter.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
public class StaticTypeFactory extends TypeFactory
{
    /**
     * The type factory method.
     */
    private Method method;

    /**
     * Create a new StaticTypeFactory.
     *
     * @param method A static method that takes one string argument.
     */
    public StaticTypeFactory(Method method) {
        this.method = method;
    }

    /* Inherited. */
    public Object build(String string) {
        if (string == null)
            return null;

        try {
            try {
                return method.invoke(null, string);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new RuntimeException("Type build error", th);
        }
    }
}

