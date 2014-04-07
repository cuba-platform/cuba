/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

/**
 * Entity for working with configuration parameters (see com.haulmont.cuba.core.config.Config).<br>
 * Should not be used in application code.
 */
@Entity(name = "sys$Config")
@Table(name = "SYS_CONFIG")
@SystemLevel
public class Config extends BaseUuidEntity implements Versioned, Updatable {
    private static final long serialVersionUID = -2103060811330948816L;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "UPDATE_TS")
    private Date updateTs;

    @Column(name = "UPDATED_BY", length = LOGIN_FIELD_LEN)
    private String updatedBy;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private String value;

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Date getUpdateTs() {
        return updateTs;
    }

    @Override
    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}