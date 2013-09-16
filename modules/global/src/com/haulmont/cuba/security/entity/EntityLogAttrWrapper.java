/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

@MetaClass(name = "sec$EntityLogAttrWrapper")
@SystemLevel
public class EntityLogAttrWrapper extends AbstractNotPersistentEntity {

    @MetaProperty
    private EntityLogAttr entityLogAttr;

    @MetaProperty
    private BaseUuidEntity valueAttr;

    public EntityLogAttrWrapper(EntityLogAttr entityLogAttr, BaseUuidEntity valueAttr) {
        this.entityLogAttr = entityLogAttr;
        this.valueAttr = valueAttr;
    }

    public EntityLogAttrWrapper(EntityLogAttr entityLogAttr) {
        this.entityLogAttr = entityLogAttr;
    }

    public EntityLogAttr getEntityLogAttr() {
        return entityLogAttr;
    }

    public void setEntityLogAttr(EntityLogAttr entityLogAttr) {
        this.entityLogAttr = entityLogAttr;
    }

    public BaseUuidEntity getValueAttr() {
        return valueAttr;
    }

    public void setValueAttr(BaseUuidEntity valueAttr) {
        this.valueAttr = valueAttr;
    }

    @MetaProperty
    public String getDisplayValue() {
        return valueAttr == null ? entityLogAttr.getDisplayValue() : valueAttr.getInstanceName();
    }
}
