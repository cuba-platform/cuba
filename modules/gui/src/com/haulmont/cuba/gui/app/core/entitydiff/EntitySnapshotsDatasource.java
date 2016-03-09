/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.app.EntitySnapshotService;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;

import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class EntitySnapshotsDatasource extends CustomCollectionDatasource<EntitySnapshot, UUID> {

    protected BaseEntity entity;
    protected List<EntitySnapshot> snapshots;

    @Override
    public boolean isModified() {
        return false;
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }

    @Override
    protected Collection<EntitySnapshot> getEntities(Map<String, Object> params) {
        if (entity != null) {
            EntitySnapshotService snapshotService = AppBeans.get(EntitySnapshotService.NAME);
            snapshots = snapshotService.getSnapshots(entity.getMetaClass(), entity.getUuid());
            return snapshots;
        }
        return Collections.emptyList();
    }

    public EntitySnapshot getLatestSnapshot() {
        if ((snapshots != null) && (snapshots.size() > 0))
            return snapshots.get(snapshots.size() - 1);
        return null;
    }
}