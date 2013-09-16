/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config;

import java.lang.annotation.*;

/**
 * Annotation specifying the prefix for all configuration fields in an interface.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Prefix
{
    /**
     * The prefix.
     */
    public String value();
}
