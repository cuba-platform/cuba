/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 28.03.11 15:51
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.UuidProvider;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.UUID;

@MappedSuperclass
public abstract class EmbeddableEntity extends AbstractInstance implements Entity<UUID> {
    private static final long serialVersionUID = 266201862280559076L;

    @Id
    @Transient
    private UUID id;

    public UUID getId() {
        return id;
    }

    protected EmbeddableEntity() {
        id = UuidProvider.createUuid();
    }

    @Override
    public MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(getClass());
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