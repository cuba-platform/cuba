/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A factory that builds a type by invoking its constructor, supplying the
 * string value as a parameter.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
public class ConstructorTypeFactory extends TypeFactory
{
    /**
     * The type constructor.
     */
    private Constructor constructor;

    /**
     * Create a new ConstructorTypeFactory.
     *
     * @param constructor A constructor that takes a single string argument.
     */
    public ConstructorTypeFactory(Constructor constructor) {
        this.constructor = constructor;
    }

    /* Inherited. */
    public Object build(String string) {
        if (string == null)
            return null;

        try {
            try {
                return constructor.newInstance(string);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new RuntimeException("Type construct error", th);
        }
    }
}
