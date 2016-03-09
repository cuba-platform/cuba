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
import com.haulmont.cuba.core.sys.CubaEnhancingDisabled;
import org.apache.commons.lang.ObjectUtils;

import java.util.UUID;

/**
 * Base class for not persistent entities.
 *
 * @author Grachev
 * @version $Id$
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "AbstractNotPersistentEntity")
public abstract class AbstractNotPersistentEntity extends AbstractInstance implements Entity<UUID>, CubaEnhancingDisabled {

    private static final long serialVersionUID = -2846020822531467401L;

    protected UUID id;

    protected AbstractNotPersistentEntity() {
        id = UuidProvider.createUuid();
    }

    @Override
    public UUID getUuid() {
        return id;
    }

    @Override
    public MetaClass getMetaClass() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getSession().getClassNN(getClass());
    }

    @MetaProperty
    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
