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
 * <p>$Id$</p>
 *
 * @author artamonov
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
    EntityDiff getDifference(EntitySnapshot first, EntitySnapshot second);

    /**
     * Translate snapshots for archival classes
     *
     * @param metaClass    Metaclass
     * @param id           Entity Id
     * @param classMapping Map of [OldClass -> NewClass] for migration
     */
    void migrateSnapshots(MetaClass metaClass, UUID id, Map<Class, Class> classMapping);
}