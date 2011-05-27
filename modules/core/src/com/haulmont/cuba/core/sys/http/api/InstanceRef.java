/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.http.api;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.EntityLoadInfo;

import java.util.UUID;

/**
 * Author: Alexander Chevelev
 * Date: 14.05.2011
 * Time: 1:18:58
 */
public class InstanceRef {
    private EntityLoadInfo loadInfo;
    private BaseUuidEntity instance;

    public InstanceRef(EntityLoadInfo loadInfo) throws InstantiationException, IllegalAccessException {
        if (loadInfo == null)
            throw new NullPointerException("No load info passed");

        this.loadInfo = loadInfo;
        MetaClass childMetaClass = this.loadInfo.getMetaClass();
        instance = childMetaClass.createInstance();
        instance.setId(this.loadInfo.getId());
    }

    public UUID getId() {
        return loadInfo.getId();
    }

    public MetaClass getMetaClass() {
        return loadInfo.getMetaClass();
    }

    public BaseUuidEntity getInstance() {
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
