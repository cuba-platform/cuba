/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UuidProvider;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
@MappedSuperclass
public abstract class EmbeddableEntity extends AbstractInstance implements Entity<UUID> {
    private static final long serialVersionUID = 266201862280559076L;

    @Id
    @Transient
    private UUID id;

    @Override
    public UUID getId() {
        return id;
    }

    public EmbeddableEntity() {
        id = UuidProvider.createUuid();
    }

    @Override
    public MetaClass getMetaClass() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getSession().getClass(getClass());
    }

    @Override
    public UUID getUuid() {
        return id;
    }

    @Override
    public String toString() {
        return getClass().getName() + "-" + id;
    }
}