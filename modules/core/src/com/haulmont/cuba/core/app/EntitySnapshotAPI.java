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
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface EntitySnapshotAPI {

    String NAME = "cuba_EntitySnapshotManager";

    List<EntitySnapshot> getSnapshots(MetaClass metaClass, UUID id);

    EntitySnapshot createSnapshot(BaseEntity entity, View view);

    BaseEntity extractEntity(EntitySnapshot snapshot);

    EntityDiff getDifference(EntitySnapshot first, EntitySnapshot second);
}
