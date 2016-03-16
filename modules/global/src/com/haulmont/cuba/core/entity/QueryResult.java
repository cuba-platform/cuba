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
import com.haulmont.cuba.core.sys.CubaEnhanced;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.UUID;

/**
 */
@Entity(name = "sys$QueryResult")
@Table(name = "SYS_QUERY_RESULT")
@SystemLevel
public class QueryResult implements CubaEnhanced { // Marker interface added here to avoid CUBA-specific enhancing

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SESSION_ID")
    private UUID sessionId;

    @Column(name = "QUERY_KEY")
    private Integer queryKey;

    @Column(name = "ENTITY_ID")
    private UUID entityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
