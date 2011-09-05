/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.config;

import java.lang.annotation.*;

/**
 * Annotation specifying the prefix for all configuration fields in an interface.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Prefix
{
    /**
     * The prefix.
     */
    public String value();
}
