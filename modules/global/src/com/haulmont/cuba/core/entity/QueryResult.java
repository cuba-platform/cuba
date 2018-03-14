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

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.UUID;

@Entity(name = "sys$QueryResult")
@Table(name = "SYS_QUERY_RESULT")
@SystemLevel
public class QueryResult extends BaseIdentityIdEntity {

    @Column(name = "SESSION_ID")
    private UUID sessionId;

    @Column(name = "QUERY_KEY")
    private Integer queryKey;

    @Column(name = "ENTITY_ID")
    private UUID entityId;

    @Column(name = "STRING_ENTITY_ID", length = 255)
    protected String stringEntityId;

    @Column(name = "INT_ENTITY_ID")
    protected Integer intEntityId;

    @Column(name = "LONG_ENTITY_ID")
    protected Long longEntityId;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(Integer queryKey) {
        this.queryKey = queryKey;
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
}