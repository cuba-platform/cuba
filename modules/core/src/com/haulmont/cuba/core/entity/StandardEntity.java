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

@MappedSuperclass
public class StandardEntity
        extends BaseUuidEntity
        implements Versioned, Updatable, DeleteDeferred
{
    @Version
    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "UPDATE_TS")
    private Date updateTs;

    @Column(name = "UPDATED_BY", length = PersistenceProvider.LOGIN_FIELD_LEN)
    private String updatedBy;

    @Column(name = "IS_DELETED")
    private Boolean deleted = false;

    @Column(name = "DELETED_BY", length = PersistenceProvider.LOGIN_FIELD_LEN)
    private String deletedBy;

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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }
}
