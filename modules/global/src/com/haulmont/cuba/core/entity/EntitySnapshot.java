/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateTimeDatatype;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;

/**
 * Snapshot for entity.
 *
 * @author artamonov
 * @version $Id$
 */
@Entity(name = "sys$EntitySnapshot")
@Table(name = "SYS_ENTITY_SNAPSHOT")
@SystemLevel
public class EntitySnapshot extends BaseUuidEntity {

    private static final long serialVersionUID = 4835363127711391591L;

    @Column(name = "VIEW_XML")
    private String viewXml;

    @Column(name = "SNAPSHOT_XML")
    private String snapshotXml;

    @Column(name = "ENTITY_META_CLASS")
    private String entityMetaClass;

    @Column(name = "SNAPSHOT_DATE")
    private Date snapshotDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @Column(name = "ENTITY_ID")
    private UUID entityId;

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

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    @MetaProperty
    public String getLabel() {
        String name = "";
        if (author != null && StringUtils.isNotEmpty(this.author.getCaption())) {
           name += this.author.getCaption() + " ";
        }

        Datatype datatype = Datatypes.get(DateTimeDatatype.NAME);

        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
        if (userSessionSource != null && userSessionSource.checkCurrentUserSession()) {
            name += datatype.format(snapshotDate, userSessionSource.getLocale());
        }

        return StringUtils.trim(name);
    }

    @MetaProperty
    public Date getChangeDate() {
        return this.snapshotDate;
    }

    public Date getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
    }
}