/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
