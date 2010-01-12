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
 * Global time provider. Must be used everywhere instead of <code>new Date()</code
 */
public abstract class TimeProvider
{
    private static TimeProvider getInstance() {
        return AppContext.getApplicationContext().getBean("cuba_TimeProvider", TimeProvider.class);
    }

    public static Date currentTimestamp() {
        return getInstance().__currentTimestamp();
    }

    protected abstract Date __currentTimestamp();
}
