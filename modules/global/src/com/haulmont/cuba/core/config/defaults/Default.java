/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config.defaults;

import java.lang.annotation.*;

/**
 * A default value. This is used for all types that do not have a native default
 * annotation but can be built from a string value.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Default
{
    /**
     * The default value.
     */
    public String value();
}
