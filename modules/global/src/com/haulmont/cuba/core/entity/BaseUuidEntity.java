/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Base class for persistent entities with UUID identifier.<br>
 * Inherit from it if you need an entity without optimistic locking, update and soft deletion info.
 *
 * @author krivopustov
 * @version $Id$
 */
@MappedSuperclass
public abstract class BaseUuidEntity extends BaseGenericIdEntity<UUID> {

    private static final long serialVersionUID = -2217624132287086972L;

    @Id
    @Column(name = "ID")
    @Persistent
    protected UUID id;

    public BaseUuidEntity() {
        id = UuidProvider.createUuid();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getUuid() {
        return id;
    }
}