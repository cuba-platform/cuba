/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 11:57:54
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.Session;

public abstract class MetadataProvider
{
    public static final String IMPL_PROP = "cuba.MetadataProvider.impl";

    private static final String DEFAULT_IMPL = "com.haulmont.cuba.core.impl.MetadataProviderImpl";

    private static MetadataProvider instance;

    private static MetadataProvider getInstance() {
        if (instance == null) {
            String implClassName = System.getProperty(IMPL_PROP);
            if (implClassName == null)
                implClassName = DEFAULT_IMPL;
            try {
                Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                instance = (MetadataProvider) implClass.newInstance();
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

    public static Session getSession() {
        return getInstance().__getSession();
    }

    protected abstract Session __getSession();
}
