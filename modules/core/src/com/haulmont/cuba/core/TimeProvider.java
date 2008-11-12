/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 14:29:14
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.impl.TimeProviderImpl;

import java.util.Date;

public abstract class TimeProvider
{
    private static TimeProvider instance;

    private static TimeProvider getInstance() {
        if (instance == null) {
            instance = new TimeProviderImpl();
        }
        return instance;
    }

    public static Date currentTimestamp() {
        return getInstance().__currentTimestamp();
    }

    protected abstract Date __currentTimestamp();
}
