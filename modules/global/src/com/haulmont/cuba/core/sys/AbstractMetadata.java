/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.ViewRepository;

import java.util.Collection;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractMetadata implements Metadata {

    protected volatile Session session;

    protected volatile ViewRepository viewRepository;

    protected volatile Map<Class, Class> replacedEntities;

    @Override
    public Session getSession() {
        if (session == null) {
            synchronized (this) {
                if (session == null) {
                    session = initMetadata();
                }
            }
        }
        return session;
    }

    @Override
    public ViewRepository getViewRepository() {
        if (viewRepository == null) {
            synchronized (this) {
                if (viewRepository == null) {
                    viewRepository = initViews();
                }
            }
        }
        return viewRepository;
    }

    @Override
    public Map<Class, Class> getReplacedEntities() {
        if (replacedEntities == null) {
            synchronized (this) {
                if (replacedEntities == null) {
                    replacedEntities = initReplacedEntities();
                }
            }
        }
        return replacedEntities;
    }

    protected void loadMetadata(MetadataLoader loader, Collection<String> packages) {
        for (String p : packages) {
            loader.loadPackage(p, p);
        }
    }
    protected <T> T __create(Class<T> entityClass) {
        Class<T> replace = getReplacedEntities().get(entityClass);
        if (replace == null)
            replace = entityClass;
        try {
            T obj = replace.newInstance();
            return obj;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> Class<T> __getReplacedClass(Class<T> clazz) {
        Class replacedClass = getReplacedEntities().get(clazz);
        return replacedClass == null ? clazz : replacedClass;
    }

    public <T> T create(Class<T> entityClass) {
        return (T) __create(entityClass);
    }

    public <T> T create(MetaClass metaClass) {
        return (T) __create(metaClass.getJavaClass());
    }

    public <T> T create(String entityName) {
        MetaClass metaClass = getSession().getClass(entityName);
        return (T) __create(metaClass.getJavaClass());
    }

    public <T> Class<T> getReplacedClass(Class<T> entityClass) {
        return __getReplacedClass(entityClass);
    }

    public <T> Class<T> getReplacedClass(MetaClass metaClass) {
        return __getReplacedClass(metaClass.getJavaClass());
    }

    public <T> Class<T> getReplacedClass(String entityName) {
        MetaClass metaClass = getSession().getClass(entityName);
        return __getReplacedClass(metaClass.getJavaClass());
    }

    protected abstract Session initMetadata();

    protected abstract ViewRepository initViews();

    protected abstract Map<Class,Class> initReplacedEntities();

}
