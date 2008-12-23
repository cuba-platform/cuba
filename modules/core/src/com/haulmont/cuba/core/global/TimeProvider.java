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

import java.util.Date;

public abstract class TimeProvider
{

    public static final String IMPL_PROP = "cuba.TimeProvider.impl";

    private static final String DEFAULT_IMPL = "com.haulmont.cuba.core.sys.TimeProviderImpl";

    private static TimeProvider instance;

    private static TimeProvider getInstance() {
        if (instance == null) {
            String implClassName = System.getProperty(IMPL_PROP);
            if (implClassName == null)
                implClassName = DEFAULT_IMPL;
            try {
                Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                instance = (TimeProvider) implClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public static Date currentTimestamp() {
        return getInstance().__currentTimestamp();
    }

    protected abstract Date __currentTimestamp();
}
