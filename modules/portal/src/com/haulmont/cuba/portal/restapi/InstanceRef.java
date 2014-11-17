/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityLoadInfo;

import java.util.UUID;

/**
 * @author chevelev
 * @version $Id$
 */
public class InstanceRef {
    private EntityLoadInfo loadInfo;
    private BaseGenericIdEntity<Object> instance;

    public InstanceRef(EntityLoadInfo loadInfo) throws InstantiationException, IllegalAccessException {
        if (loadInfo == null)
            throw new NullPointerException("No load info passed");

        this.loadInfo = loadInfo;
        MetaClass childMetaClass = this.loadInfo.getMetaClass();
        instance = childMetaClass.createInstance();
        if (!loadInfo.isNewEntity()) {
            for (MetaProperty metaProperty : childMetaClass.getProperties()) {
                if (!metaProperty.getRange().isClass()) {
                    try {
                        instance.setValue(metaProperty.getName(), null);
                    } catch (Exception e) {
                    }
                }
            }
        }
        instance.setId(this.loadInfo.getId());
    }

    public Object getId() {
        return loadInfo.getId();
    }

    public MetaClass getMetaClass() {
        return loadInfo.getMetaClass();
    }

    public Entity getInstance() {
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceRef that = (InstanceRef) o;

        if (!loadInfo.getId().equals(that.loadInfo.getId())) return false;
        if (!loadInfo.getMetaClass().equals(that.loadInfo.getMetaClass())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = loadInfo.getId().hashCode();
        result1 = 31 * result1 + loadInfo.getMetaClass().hashCode();
        return result1;
    }
}