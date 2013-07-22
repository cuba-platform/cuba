/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hasanov
 * @version $Id$
 */
@Retention(RUNTIME)
public @interface WindowParam {
    String name() default "";
    boolean required() default false;
}
