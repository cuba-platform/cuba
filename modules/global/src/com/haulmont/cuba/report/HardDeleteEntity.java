/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:32:18
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Versioned;
import com.haulmont.cuba.core.entity.Updatable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Column;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * Optimistically locked, implements Updatable
 */
@MappedSuperclass
public class HardDeleteEntity
        extends BaseUuidEntity
        implements Versioned, Updatable
{
    private static final long serialVersionUID = -7868980589172607336L;

    @Version
    @Column(name = "VERSION")
    protected Integer version;

    @Column(name = "UPDATE_TS")
    protected Date updateTs;

    @Column(name = "UPDATED_BY", length = LOGIN_FIELD_LEN)
    protected String updatedBy;

    public Integer getVersion() {
        return version;
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
}
