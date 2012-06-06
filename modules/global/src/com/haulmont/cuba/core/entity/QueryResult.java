/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
//@IdClass(QueryResult.Id.class)
@SystemLevel
public class QueryResult implements CubaEnhanced { // Marker interface added here to avoid CUBA-specific enhancing

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

//    @javax.persistence.Id
    @Persistent
    @Column(name = "SESSION_ID")
    private UUID sessionId;

//    @javax.persistence.Id
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

//    public static class Id {
//        public UUID sessionId;
//        public Integer queryKey;
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Id id = (Id) o;
//
//            if (queryKey != null ? !queryKey.equals(id.queryKey) : id.queryKey != null) return false;
//            if (sessionId != null ? !sessionId.equals(id.sessionId) : id.sessionId != null) return false;
//
//            return true;
//        }
//
//        @Override
//        public int hashCode() {
//            int result = sessionId != null ? sessionId.hashCode() : 0;
//            result = 31 * result + (queryKey != null ? queryKey.hashCode() : 0);
//            return result;
//        }
//    }
}
