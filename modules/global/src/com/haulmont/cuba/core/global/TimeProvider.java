/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AppContext;

import java.util.Date;

/**
 * DEPRECATED - use {@link TimeSource} via DI or <code>AppBeans.get(TimeSource.class)</code>
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
public abstract class TimeProvider
{
    public static Date currentTimestamp() {
        if (AppContext.getApplicationContext() == null)
            return null;

        return AppBeans.get(TimeSource.NAME, TimeSource.class).currentTimestamp();
    }

    public static long currentTimeMillis() {
        if (AppContext.getApplicationContext() == null)
            return 0;

        return AppBeans.get(TimeSource.NAME, TimeSource.class).currentTimeMillis();
    }
}
