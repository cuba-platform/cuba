/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.config.defaults;

import java.lang.annotation.*;

/**
 * A float default value.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DefaultFloat
{
    /**
     * The default value.
     */
    public float value();
}
