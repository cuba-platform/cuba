/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:32:18
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.PersistenceProvider;

import javax.persistence.MappedSuperclass;
import javax.persistence.Column;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

import org.apache.openjpa.persistence.Persistent;

@MappedSuperclass
public class StandardEntity
        extends BaseUuidEntity
        implements Versioned, Updatable, DeleteDeferred
{
    @Version
    @Column(name = "VERSION")
    protected Integer version;

    @Column(name = "UPDATE_TS")
    protected Date updateTs;

    @Persistent
    @Column(name = "UPDATED_BY")
    protected UUID updatedBy;

    @Column(name = "DELETE_TS")
    protected Date deleteTs;

    @Persistent
    @Column(name = "DELETED_BY")
    protected UUID deletedBy;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean isDeleted() {
        return deleteTs != null;
    }

    public Date getDeleteTs() {
        return deleteTs;
    }

    public void setDeleteTs(Date deleteTs) {
        this.deleteTs = deleteTs;
    }

    public UUID getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(UUID deletedBy) {
        this.deletedBy = deletedBy;
    }
}
