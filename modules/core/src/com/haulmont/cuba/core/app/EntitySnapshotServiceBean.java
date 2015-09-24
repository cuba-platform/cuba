/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.entity.diff.EntityDiff;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
@Service(EntitySnapshotService.NAME)
public class EntitySnapshotServiceBean implements EntitySnapshotService {

    @Inject
    protected EntitySnapshotAPI snapshotAPI;

    @Override
    public List<EntitySnapshot> getSnapshots(MetaClass metaClass, UUID id) {
        return snapshotAPI.getSnapshots(metaClass, id);
    }

    @Override
    public EntitySnapshot createSnapshot(BaseEntity entity, View view) {
        return snapshotAPI.createSnapshot(entity, view);
    }

    @Override
    public EntitySnapshot createSnapshot(BaseEntity entity, View view, Date snapshotDate) {
        return snapshotAPI.createSnapshot(entity, view, snapshotDate);
    }

    @Override
    public EntitySnapshot createSnapshot(BaseEntity entity, View view, Date snapshotDate, User author) {
        return snapshotAPI.createSnapshot(entity, view, snapshotDate, author);
    }

    @Override
    public BaseEntity extractEntity(EntitySnapshot snapshot) {
        return snapshotAPI.extractEntity(snapshot);
    }

    @Override
    public EntityDiff getDifference(@Nullable EntitySnapshot first, EntitySnapshot second) {
        return snapshotAPI.getDifference(first, second);
    }

    @Override
    public void migrateSnapshots(MetaClass metaClass, UUID id, Map<Class, Class> classMapping) {
        snapshotAPI.migrateSnapshots(metaClass, id, classMapping);
    }
}