/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifies injectable fields in screen controllers, value for field comes from screen parameters
 *
 * @author hasanov
 * @version $Id$
 */
@Target(value = ElementType.FIELD)
@Retention(RUNTIME)
public @interface WindowParam {
    String name() default "";
    boolean required() default false;
}
