/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.global.UuidProvider;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Base class for persistent entities with Long identifier.
 *
 * @author krivopustov
 * @version $Id$
 */
@MappedSuperclass
public abstract class BaseLongIdEntity extends BaseGenericIdEntity<Long> {

    private static final long serialVersionUID = 1748237513475338490L;

    @Id
    @Column(name = "ID")
    protected Long id;

    @Column(name = "UUID", nullable = false)
    protected UUID uuid;

    protected BaseLongIdEntity() {
        uuid = UuidProvider.createUuid();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}