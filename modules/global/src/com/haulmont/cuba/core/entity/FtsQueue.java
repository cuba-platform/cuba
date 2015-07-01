/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "sys$FtsQueue")
@Table(name = "SYS_FTS_QUEUE")
@SystemLevel
public class FtsQueue extends BaseUuidEntity {

    private static final long serialVersionUID = 6488459370269702942L;

    @Column(name = "ENTITY_ID")
    protected UUID entityId;

    @Column(name = "ENTITY_NAME")
    protected String entityName;

    @Column(name = "CHANGE_TYPE")
    protected String changeType;

    @Column(name = "SOURCE_HOST")
    protected String sourceHost;

    @Column(name = "INDEXING_HOST")
    protected String indexingHost;

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public FtsChangeType getChangeType() {
        return FtsChangeType.fromId(changeType);
    }

    public void setChangeType(FtsChangeType changeType) {
        this.changeType = changeType.getId();
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    public String getIndexingHost() {
        return indexingHost;
    }

    public void setIndexingHost(String indexingHost) {
        this.indexingHost = indexingHost;
    }
}
