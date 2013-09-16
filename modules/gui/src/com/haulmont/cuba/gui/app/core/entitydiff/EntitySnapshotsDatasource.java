/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.app.EntitySnapshotService;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class EntitySnapshotsDatasource extends CollectionDatasourceImpl<EntitySnapshot, UUID> {

    private BaseEntity entity;
    private List<EntitySnapshot> snapshots;

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
    protected void loadData(Map<String, Object> params) {
        clear();

        if (entity != null) {
            EntitySnapshotService snapshotService = ServiceLocator.lookup(EntitySnapshotService.NAME);
            snapshots = snapshotService.getSnapshots(
                    MetadataProvider.getSession().getClass(entity.getClass()),
                    entity.getUuid());

            for (EntitySnapshot snapshot : snapshots) {
                data.put(snapshot.getId(), snapshot);
                attachListener(snapshot);
            }
        }
    }

    public EntitySnapshot getLatestSnapshot() {
        if ((snapshots != null) && (snapshots.size() > 0))
            return snapshots.get(snapshots.size() - 1);
        return null;
    }
}
