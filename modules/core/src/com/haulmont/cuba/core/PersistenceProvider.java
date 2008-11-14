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

import javax.persistence.Entity;
import java.lang.annotation.Annotation;

import org.jboss.remoting.samples.chat.exceptions.InvalidArgumentException;
import org.apache.commons.lang.StringUtils;

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

    public static String getEntityName(Class entityClass) {
        Annotation annotation = entityClass.getAnnotation(Entity.class);
        if (annotation == null)
            throw new IllegalArgumentException("Class " + entityClass + " is not an entity");
        String name = ((Entity) annotation).name();
        if (!StringUtils.isEmpty(name))
            return name;
        else
            return entityClass.getSimpleName();
    }

    protected abstract EntityManagerFactoryAdapter __getEntityManagerFactory();

    protected abstract EntityManagerAdapter __getEntityManager();
}
