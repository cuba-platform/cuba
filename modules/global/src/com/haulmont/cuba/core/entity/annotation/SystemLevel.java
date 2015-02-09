/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that annotated entity/field is low-level and should not be available for end-user in various entity/field lists.<br/>
 * For field indicates that attribute should not be available for dynamic filters in UI.
 *
 * @author krivopustov
 * @version $Id$
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLevel {

    boolean value() default true;
}