/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to define a non-persistent attribute, or to specify additional properties of a persistent
 * attribute.
 *
 * @author abramov
 * @version $Id$
 */
@Target({java.lang.annotation.ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaProperty {

    /**
     * Whether the attribute is required.
     */
    boolean mandatory() default false;

    /**
     * Explicitly defined datatype that overrides a datatype inferred from the attribute Java type.
     */
    String datatype() default "";

    /**
     * Related properties are fetched from the database when this property is included in a view.
     */
    String[] related() default "";
}
