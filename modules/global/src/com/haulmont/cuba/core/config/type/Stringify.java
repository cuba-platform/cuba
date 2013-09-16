/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config.type;

import java.lang.annotation.*;

/**
 * Annotation that identifies how to convert a type to a string. This
 * can be specified with either an instance method that converts an
 * object to its primitive (ultimately string) representation, or else a
 * TypeStringify class that performs this operation. Only one value should
 * be specified. When applied to a class, applies to the class itself; when
 * applied to a method, applies to the method's return type.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Stringify
{
    /**
     * The name of a method that can convert an object to its primitive
     * (ultimately string) form.
     */
    public String method() default "";

    /**
     * A class that can convert an object to its string form.
     */
    public Class<? extends TypeStringify> stringify();
}
