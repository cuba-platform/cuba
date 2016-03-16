/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.app.EntitySnapshotService;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;

import java.util.*;

/**
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