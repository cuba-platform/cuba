/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.06.2010 14:38:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import java.util.UUID;

@Entity(name = "sys$FtsQueue")
@Table(name = "SYS_FTS_QUEUE")
@SystemLevel
public class FtsQueue extends BaseUuidEntity {

    private static final long serialVersionUID = 6488459370269702942L;

    @Persistent
    @Column(name = "ENTITY_ID")
    private UUID entityId;

    @Column(name = "ENTITY_NAME")
    private String entityName;

    @Column(name = "CHANGE_TYPE")
    private String changeType;

    @Column(name = "SOURCE_HOST")
    private String sourceHost;

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
}
