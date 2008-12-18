/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 11:49:00
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import java.util.UUID;

public abstract class UuidProvider
{
    public static final String IMPL_PROP = "cuba.UuidProvider.sys";

    private static final String DEFAULT_IMPL = "com.haulmont.cuba.core.sys.UuidProviderImpl";

    private static UuidProvider instance;

    private static UuidProvider getInstance() {
        if (instance == null) {
            String implClassName = System.getProperty(IMPL_PROP);
            if (implClassName == null)
                implClassName = DEFAULT_IMPL;
            try {
                Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                instance = (UuidProvider) implClass.newInstance();
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

    public static UUID createUuid() {
        return getInstance().__createUuid();
    }

    protected abstract UUID __createUuid();
}
