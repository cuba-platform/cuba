/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.sys.CubaEnhanced;
import org.apache.commons.lang.ObjectUtils;

import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Base class for not persistent entities.
 *
 * @author Grachev
 * @version $Id$
 */
public abstract class AbstractNotPersistentEntity extends AbstractInstance implements Entity<UUID> {

    private static final long serialVersionUID = -2846020822531467401L;

    protected UUID uuid;

    protected AbstractNotPersistentEntity() {
        uuid = UuidProvider.createUuid();
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public MetaClass getMetaClass() {
        return AppBeans.get(Metadata.class).getSession().getClass(getClass());
    }

    @MetaProperty
    @Override
    public UUID getId() {
        return uuid;
    }

    public void setId(UUID id) {
        this.uuid = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractNotPersistentEntity that = (AbstractNotPersistentEntity) o;

        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
