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

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity(name = "sys$FtsQueue")
@Table(name = "SYS_FTS_QUEUE")
@SystemLevel
public class FtsQueue extends BaseUuidEntity implements Creatable {

    private static final long serialVersionUID = 6488459370269702942L;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = LOGIN_FIELD_LEN)
    protected String createdBy;

    @Column(name = "ENTITY_ID")
    protected UUID entityId;

    @Column(name = "STRING_ENTITY_ID", length = 255)
    protected String stringEntityId;

    @Column(name = "INT_ENTITY_ID")
    protected Integer intEntityId;

    @Column(name = "LONG_ENTITY_ID")
    protected Long longEntityId;

    @Column(name = "ENTITY_NAME")
    protected String entityName;

    @Column(name = "CHANGE_TYPE")
    protected String changeType;

    @Column(name = "SOURCE_HOST")
    protected String sourceHost;

    @Column(name = "INDEXING_HOST")
    protected String indexingHost;

    @Column(name = "FAKE")
    protected Boolean fake = false;

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

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getStringEntityId() {
        return stringEntityId;
    }

    public void setStringEntityId(String stringEntityId) {
        this.stringEntityId = stringEntityId;
    }

    public Integer getIntEntityId() {
        return intEntityId;
    }

    public void setIntEntityId(Integer intEntityId) {
        this.intEntityId = intEntityId;
    }

    public Long getLongEntityId() {
        return longEntityId;
    }

    public void setLongEntityId(Long longEntityId) {
        this.longEntityId = longEntityId;
    }

    public Object getObjectEntityId() {
        if (entityId != null) {
            return entityId;
        } else if (longEntityId != null) {
            return longEntityId;
        } else if (intEntityId != null) {
            return intEntityId;
        } else if (stringEntityId != null) {
            return stringEntityId;
        } else {
            return null;
        }
    }

    public void setObjectEntityId(Object objectEntityId) {
        if (objectEntityId instanceof UUID) {
            setEntityId((UUID) objectEntityId);
        } else if (objectEntityId instanceof Long) {
            setLongEntityId((Long) objectEntityId);
        } else if (objectEntityId instanceof Integer) {
            setIntEntityId((Integer) objectEntityId);
        } else if (objectEntityId instanceof String) {
            setStringEntityId((String) objectEntityId);
        } else if (objectEntityId instanceof IdProxy) {
            setLongEntityId(((IdProxy) objectEntityId).getNN());
        }
        else if (objectEntityId == null) {
            setEntityId(null);
            setLongEntityId(null);
            setIntEntityId(null);
            setLongEntityId(null);
        } else {
            throw new IllegalArgumentException(
                    String.format("Unsupported primary key type: %s", objectEntityId.getClass().getSimpleName()));
        }
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

    public Boolean getFake() {
        return fake;
    }

    public void setFake(Boolean fake) {
        this.fake = fake;
    }
}
