/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines additional parameters for Embedded attributes.
 *
 * @author Konstantin Krivopustov
 * @version $Id$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface EmbeddedParameters {

    /**
     * If false, the embedded entity can not be null. This means you can always provide an instance when persisting
     * the entity, even if all its attributes are null.
     * <p>
     * By default, the embedded entity can be null. In this case ORM does not create the instance on loading
     * if all attributes are null.
     */
    boolean nullAllowed() default true;
}
