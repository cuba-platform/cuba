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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Create and analyze entity snapshots
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@SuppressWarnings({"unused"})
public interface EntitySnapshotAPI {

    String NAME = "cuba_EntitySnapshotManager";

    /**
     * Load snapshots for entity
     *
     * @param metaClass Entity metaclass
     * @param id        Entity Id
     * @return Snapshot list
     */
    List<EntitySnapshot> getSnapshots(MetaClass metaClass, UUID id);

    /**
     * Translate snapshots for archival classes
     *
     * @param metaClass    Metaclass
     * @param id           Entity Id
     * @param classMapping Map of [OldClass -> NewClass] for migration
     */
    void migrateSnapshots(MetaClass metaClass, UUID id, Map<Class, Class> classMapping);

    /**
     * Create snapshot for Entity and store it to database
     *
     * @param entity Entity
     * @param view   View
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
     * Restore entity by snapshot
     *
     * @param snapshot Snapshot
     * @return Entity instance
     */
    BaseEntity extractEntity(EntitySnapshot snapshot);

    /**
     * Restore view from snapshot
     *
     * @param snapshot Snapshot
     * @return View instance
     */
    public View extractView(EntitySnapshot snapshot);

    /**
     * Diff two versions of entity
     *
     * @param first  First version
     * @param second Second version
     * @return Diffs
     */
    EntityDiff getDifference(EntitySnapshot first, EntitySnapshot second);
}