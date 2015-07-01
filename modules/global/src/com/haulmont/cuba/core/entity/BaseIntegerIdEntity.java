/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.global.UuidProvider;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Base class for persistent entities with Integer identifier.
 *
 * @author krivopustov
 * @version $Id$
 */
@MappedSuperclass
public abstract class BaseIntegerIdEntity extends BaseGenericIdEntity<Integer> {

    private static final long serialVersionUID = 1748237513475338490L;

    @Id
    @Column(name = "ID")
    protected Integer id;

    @Column(name = "UUID", nullable = false)
    protected UUID uuid;

    protected BaseIntegerIdEntity() {
        uuid = UuidProvider.createUuid();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
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