/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 14:29:14
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AppContext;

import java.util.Date;

/**
 * Global time provider for static context. Must be used everywhere instead of <code>new Date()</code.<br/>
 * Consider use of {@link TimeSource} directly.
 */
public abstract class TimeProvider
{
    public static Date currentTimestamp() {
        if (AppContext.getApplicationContext() == null)
            return null;

        return AppContext.getBean(TimeSource.NAME, TimeSource.class).currentTimestamp();
    }
}
