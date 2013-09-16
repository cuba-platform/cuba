/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A stringifier that converts a type to a string by invoking a method
 * on it and converting the result directly to a string.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
public class MethodTypeStringify extends TypeStringify
{
    /**
     * The stringify method.
     */
    private Method method;

    /**
     * Create a new MethodTypeStringify.
     *
     * @param method A method that takes no argument.
     */
    public MethodTypeStringify(Method method) {
        this.method = method;
    }

    /* Inherited. */
    public String stringify(Object value) {
        try {
            try {
                return String.valueOf(method.invoke(value));
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new RuntimeException("Type stringify error", th);
        }
    }
}
