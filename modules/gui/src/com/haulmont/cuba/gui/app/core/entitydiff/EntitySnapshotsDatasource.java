/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.EntitySnapshotService;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
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

    public EntitySnapshotsDatasource(DsContext context, DataService dataservice,
                                     String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

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
