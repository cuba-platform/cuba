/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 05.03.2010 16:09:47
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.model.Instance;

@MetaClass(name = "sec$EntityLogAttrWrapper")
public class EntityLogAttrWrapper extends AbstractNotPersistentEntity {

    @MetaProperty
    private EntityLogAttr entityLogAttr;

    @MetaProperty
    private Entity valueAttr;

    public EntityLogAttrWrapper(EntityLogAttr entityLogAttr, Entity valueAttr) {
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

    public Entity getValueAttr() {
        return valueAttr;
    }

    public void setValueAttr(Entity valueAttr) {
        this.valueAttr = valueAttr;
    }

    @MetaProperty
    public String getDisplayValue() {
        return valueAttr == null ? entityLogAttr.getDisplayValue() : ((Instance) valueAttr).getInstanceName();
    }
}
