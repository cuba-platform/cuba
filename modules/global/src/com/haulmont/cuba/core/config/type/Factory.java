/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config.type;

import java.lang.annotation.*;

/**
 * Annotation that identifies how to create a type from a string.
 * This can be specified with either a static method that converts a
 * string to an instance of the class, or else a TypeFactory class that
 * performs this operation. Only one value should be specified. When
 * applied to a class, applies to the class itself; when applied to a
 * method, applies to the method's parameter.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Factory
{
    /**
     * The name of a static method that can convert a string to an instance
     * of the class.
     */
    public String method() default "";

    /**
     * A class that can convert a string to an instance of the class.
     */
    public Class<? extends TypeFactory> factory();
}
