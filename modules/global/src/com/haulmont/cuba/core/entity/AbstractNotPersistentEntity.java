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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.sys.CubaEnhanced;
import org.apache.commons.lang.ObjectUtils;

import java.util.UUID;

/**
 * Base class for non-persistent entities
 */
public abstract class AbstractNotPersistentEntity extends AbstractInstance implements Entity<UUID> {

    private static final long serialVersionUID = -2846020822531467401L;

    private UUID uuid;

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

    public UUID getUuid() {
        return uuid;
    }

    public MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(getClass());
    }

    public UUID getId() {
        return uuid;
    }

    @Override
    public void setValue(String property, Object obj, boolean checkEquals) {
        Object oldValue = getValue(property);
        if ((!checkEquals) || (!ObjectUtils.equals(oldValue, obj))) {
            getMethodsCache().invokeSetter(this, property, obj);
            if (!(this instanceof CubaEnhanced)) {
                propertyChanged(property, oldValue, obj);
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "-" + uuid;
    }
}
