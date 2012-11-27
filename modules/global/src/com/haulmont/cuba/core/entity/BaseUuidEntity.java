/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

/**
 * Base class for persistent entities with UUID identifier.<br>
 * Inherit from it if you need an entity without optimistic locking, update and soft deletion info.
 *
 * @author krivopustov
 * @version $Id$
 */
@MappedSuperclass
public abstract class BaseUuidEntity extends AbstractInstance implements BaseEntity<UUID>
{
    private static final long serialVersionUID = -2217624132287086972L;

    @Id
    @Column(name = "ID")
    @Persistent
    protected UUID id;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = LOGIN_FIELD_LEN)
    protected String createdBy;

    public BaseUuidEntity() {
        id = UuidProvider.createUuid();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUuid() {
        return id;
    }

    @Override
    public MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(getClass());
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUuid(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseUuidEntity that = (BaseUuidEntity) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getName() + "-" + id;
    }
}
