/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.MetadataProvider;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;
import java.util.Date;

public abstract class BaseLongIdEntity extends AbstractInstance implements BaseEntity<Long>
{
    @Id
    @Column(name = "ID")
    private Long id;

    private UUID uuid;

    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "CREATED_BY")
    private String createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(getClass());
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseLongIdEntity that = (BaseLongIdEntity) o;

        return !(id != null ? !id.equals(that.id) : that.id != null) && uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + uuid.hashCode();
        return result;
    }
}
