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

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Provides entity snapshots functionality.
 *
 * @author artamonov
 * @version $Id$
 */
public interface EntitySnapshotService {

    String NAME = "cuba_EntitySnapshotService";

    /**
     * Get snapshots for entity by id
     * @param metaClass Entity meta class
     * @param id Entity id
     * @return Snapshot list
     */
    List<EntitySnapshot> getSnapshots(MetaClass metaClass, UUID id);

    /**
     * Create snapshot for entity and save it to database
     * @param entity Entity
     * @param view View
     * @return Snapshot
     */
    EntitySnapshot createSnapshot(BaseEntity entity, View view);

    /**
     * Create snapshot for Entity with specific date and store it to database
     *
     * @param entity       Entity
     * @param view         View
     * @param snapshotDate Date
     * @return Snapshot
     */
    EntitySnapshot createSnapshot(BaseEntity entity, View view, Date snapshotDate);

    /**
     * Create snapshot for Entity with specific date and author and store it to database
     *
     * @param entity       Entity
     * @param view         View
     * @param snapshotDate Date
     * @param author       Author
     * @return Snapshot
     */
    EntitySnapshot createSnapshot(BaseEntity entity, View view, Date snapshotDate, User author);

    /**
     * Get entity from snapshot
     * @param snapshot Snapshot
     * @return Entity
     */
    BaseEntity extractEntity(EntitySnapshot snapshot);

    /**
     * Get Diff for snapshots
     * @param first First snapshot
     * @param second Second snapshot
     * @return Diff
     */
    EntityDiff getDifference(@Nullable EntitySnapshot first, EntitySnapshot second);

    /**
     * Translate snapshots for archival classes
     *
     * @param metaClass    Metaclass
     * @param id           Entity Id
     * @param classMapping Map of [OldClass -> NewClass] for migration
     */
    void migrateSnapshots(MetaClass metaClass, UUID id, Map<Class, Class> classMapping);
}