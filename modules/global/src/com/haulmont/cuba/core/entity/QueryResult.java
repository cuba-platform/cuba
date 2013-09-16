/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.sys.CubaEnhanced;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
@Entity(name = "sys$QueryResult")
@Table(name = "SYS_QUERY_RESULT")
@SystemLevel
public class QueryResult implements CubaEnhanced { // Marker interface added here to avoid CUBA-specific enhancing

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Persistent
    @Column(name = "SESSION_ID")
    private UUID sessionId;

    @Column(name = "QUERY_KEY")
    private Integer queryKey;

    @Column(name = "ENTITY_ID")
    @Persistent
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
