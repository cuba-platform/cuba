/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.EntityDiff;
import com.haulmont.cuba.core.global.View;

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