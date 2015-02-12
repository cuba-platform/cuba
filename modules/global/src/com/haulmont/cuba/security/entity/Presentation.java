/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;
import java.util.Date;

/**
 * A screen presentation settings.
 *
 * @author gorodnov
 * @version $Id$
 */
@Entity(name = "sec$Presentation")
@Table(name = "SEC_PRESENTATION")
@NamePattern("%s|name")
@SystemLevel
public class Presentation extends BaseUuidEntity implements Updatable {

    @Column(name = "COMPONENT")
    private String componentId;

    @Column(name = "NAME", length = 255)
    private String name;

    @Column(name = "XML")
    @Lob
    private String xml;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "IS_AUTO_SAVE")
    private Boolean autoSave;

    @Column(name = "UPDATE_TS")
    protected Date updateTs;

    @Column(name = "UPDATED_BY", length = LOGIN_FIELD_LEN)
    protected String updatedBy;

    @Transient
    private Boolean isDefault;

    private static final long serialVersionUID = -3151934186797540532L;

    public Boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
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
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return getName();
    }
}