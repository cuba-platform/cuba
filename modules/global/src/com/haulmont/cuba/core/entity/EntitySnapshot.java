/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Snapshot for entity.
 */
@Entity(name = "sys$EntitySnapshot")
@Table(name = "SYS_ENTITY_SNAPSHOT")
@SystemLevel
public class EntitySnapshot extends BaseUuidEntity implements Creatable, TenantEntity {

    private static final long serialVersionUID = 4835363127711391591L;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    protected String createdBy;

    @Column(name = "SYS_TENANT_ID")
    protected String sysTenantId;

    @Column(name = "VIEW_XML")
    private String viewXml;

    @Column(name = "SNAPSHOT_XML")
    private String snapshotXml;

    @Column(name = "ENTITY_META_CLASS")
    private String entityMetaClass;

    @Column(name = "SNAPSHOT_DATE", nullable = false)
    private Date snapshotDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @Embedded
    @EmbeddedParameters(nullAllowed = false)
    private ReferenceToEntity entity;

    @PostConstruct
    public void init() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        entity = metadata.create(ReferenceToEntity.class);
    }

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSysTenantId() {
        return sysTenantId;
    }

    public void setSysTenantId(String sysTenantId) {
        this.sysTenantId = sysTenantId;
    }

    public String getViewXml() {
        return viewXml;
    }

    public void setViewXml(String viewXml) {
        this.viewXml = viewXml;
    }

    public String getSnapshotXml() {
        return snapshotXml;
    }

    public void setSnapshotXml(String snapshotXml) {
        this.snapshotXml = snapshotXml;
    }

    public String getEntityMetaClass() {
        return entityMetaClass;
    }

    public void setEntityMetaClass(String entityMetaClass) {
        this.entityMetaClass = entityMetaClass;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    @MetaProperty(related = {"snapshotDate,author"})
    public String getLabel() {
        String name = "";
        if (author != null && StringUtils.isNotEmpty(this.author.getCaption())) {
            name += this.author.getCaption() + " ";
        }

        Datatype datatype = Datatypes.getNN(Date.class);

        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
        if (userSessionSource != null && userSessionSource.checkCurrentUserSession()) {
            name += datatype.format(snapshotDate, userSessionSource.getLocale());
        }

        return StringUtils.trim(name);
    }

    @MetaProperty(related = "snapshotDate")
    public Date getChangeDate() {
        return this.snapshotDate;
    }

    public Date getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public ReferenceToEntity getEntity() {
        return entity;
    }

    public void setEntity(ReferenceToEntity entity) {
        this.entity = entity;
    }

    public void setObjectEntityId(Object entityId) {
        entity.setObjectEntityId(entityId);
    }
}