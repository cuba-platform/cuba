/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Ilya Grachev
 * Created: 03.06.2009 18:42:08
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.utils.MethodsCache;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.UuidProvider;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for non-persistent entities
 */
public abstract class AbstractNotPersistentEntity extends AbstractInstance implements Entity<UUID> {

    private static final long serialVersionUID = -2846020822531467401L;

    private UUID uuid;

    private static transient Map<Class, MethodsCache> methodCacheMap =
            new ConcurrentHashMap<Class, MethodsCache>();

    protected AbstractNotPersistentEntity() {
        uuid = UuidProvider.createUuid();
    }

    public static <T extends AbstractNotPersistentEntity> T create(Class<T> clazz, Entity<UUID> entity) {
        try {
            T t = clazz.newInstance();
            t.uuid = entity.getId();
            return t;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected MethodsCache getMethodsCache() {
        Class cls = getClass();
        MethodsCache cache = methodCacheMap.get(cls);
        if (cache == null) {
            cache = new MethodsCache(cls);
            methodCacheMap.put(cls, cache);
        }
        return cache;
    }

    public UUID getUuid() {
        return uuid;
    }

    public com.haulmont.chile.core.model.MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(getClass());
    }

    public UUID getId() {
        return uuid;
    }

    @Override
    public String toString() {
        return getClass().getName() + "-" + uuid;
    }
}
