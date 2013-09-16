/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.entity.diff.EntityDiff;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@Service(EntitySnapshotService.NAME)
public class EntitySnapshotServiceBean implements EntitySnapshotService {

    @Override
    public List<EntitySnapshot> getSnapshots(MetaClass metaClass, UUID id) {
        EntitySnapshotAPI snapshotAPI = Locator.lookup(EntitySnapshotAPI.NAME);
        return snapshotAPI.getSnapshots(metaClass, id);
    }

    @Override
    public EntitySnapshot createSnapshot(BaseEntity entity, View view) {
        EntitySnapshotAPI snapshotAPI = Locator.lookup(EntitySnapshotAPI.NAME);
        return snapshotAPI.createSnapshot(entity, view);
    }

    @Override
    public EntitySnapshot createSnapshot(BaseEntity entity, View view, Date snapshotDate) {
        EntitySnapshotAPI snapshotAPI = Locator.lookup(EntitySnapshotAPI.NAME);
        return snapshotAPI.createSnapshot(entity, view, snapshotDate);
    }

    @Override
    public EntitySnapshot createSnapshot(BaseEntity entity, View view, Date snapshotDate, User author) {
        EntitySnapshotAPI snapshotAPI = Locator.lookup(EntitySnapshotAPI.NAME);
        return snapshotAPI.createSnapshot(entity, view, snapshotDate, author);
    }

    @Override
    public BaseEntity extractEntity(EntitySnapshot snapshot) {
        EntitySnapshotAPI snapshotAPI = Locator.lookup(EntitySnapshotAPI.NAME);
        return snapshotAPI.extractEntity(snapshot);
    }

    @Override
    public EntityDiff getDifference(EntitySnapshot first, EntitySnapshot second) {
        EntitySnapshotAPI snapshotAPI = Locator.lookup(EntitySnapshotAPI.NAME);
        return snapshotAPI.getDifference(first, second);
    }

    @Override
    public void migrateSnapshots(MetaClass metaClass, UUID id, Map<Class, Class> classMapping) {
        EntitySnapshotAPI snapshotAPI = Locator.lookup(EntitySnapshotAPI.NAME);
        snapshotAPI.migrateSnapshots(metaClass, id, classMapping);
    }
}