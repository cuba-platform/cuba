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
import com.haulmont.cuba.core.global.MetadataProvider;

import java.util.UUID;

public abstract class AbstractNotPersistentEntity extends com.haulmont.chile.core.model.impl.AbstractInstance implements Entity<UUID>{
    private static final long serialVersionUID = -2846020822531467401L;

    private UUID uuid = UUID.randomUUID();
    private MethodsCache __cache;

    public AbstractNotPersistentEntity() {
        __cache = new MethodsCache(getClass());
    }

    protected MethodsCache getMethodsCache() {
        return __cache;
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
}
