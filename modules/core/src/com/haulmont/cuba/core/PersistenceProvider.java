/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.11.2008 13:23:09
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.impl.ManagedPersistenceProvider;

public abstract class PersistenceProvider
{
    public static final int LOGIN_FIELD_LEN = 20;

    private static PersistenceProvider instance;

    private static PersistenceProvider getInstance() {
        if (instance == null) {
            instance = new ManagedPersistenceProvider(Locator.getJndiContext());
        }
        return instance;
    }

    public static EntityManagerFactoryAdapter getEntityManagerFactory() {
        return getInstance().__getEntityManagerFactory();
    }

    public static EntityManagerAdapter getEntityManager() {
        return getInstance().__getEntityManager();
    }

    protected abstract EntityManagerFactoryAdapter __getEntityManagerFactory();

    protected abstract EntityManagerAdapter __getEntityManager();
}
