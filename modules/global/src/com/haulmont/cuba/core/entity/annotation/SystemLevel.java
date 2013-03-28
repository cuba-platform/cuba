/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
 * <p/> It is not propagated down to subclasses.
 *
 * @author krivopustov
 * @version $Id$
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLevel {

    boolean value() default true;
}