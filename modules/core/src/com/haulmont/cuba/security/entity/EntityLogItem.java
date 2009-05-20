/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 17:12:25
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;
import java.util.Set;

import org.apache.openjpa.persistence.Persistent;

@Entity(name = "sec$EntityLog")
@Table(name = "SEC_ENTITY_LOG")
public class EntityLogItem extends BaseUuidEntity
{
    private static final long serialVersionUID = 5859030306889056606L;

    public static final int VALUE_LEN = 1500;

    public enum Type implements EnumClass<String>
    {
        CREATE("C"),
        MODIFY("M"),
        DELETE("D");

        private String id;

        private Type(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public static Type fromId(String value) {
            if ("C".equals(value))
                return CREATE;
            else if ("M".equals(value))
                return MODIFY;
            else if ("D".equals(value))
                return DELETE;
            else
                return null;
        }
    }

    @Column(name = "EVENT_TS")
    private Date eventTs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "TYPE", length = 1)
    private String type;

    @Column(name = "ENTITY", length = 100)
    private String entity;

    @Column(name = "ENTITY_ID")
    @Persistent
    private UUID entityId;

    @OneToMany(mappedBy = "logItem", fetch = FetchType.LAZY)
    private Set<EntityLogAttr> attributes;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public Date getEventTs() {
        return eventTs;
    }

    public void setEventTs(Date eventTs) {
        this.eventTs = eventTs;
    }

    public Type getType() {
        return Type.fromId(type);
    }

    public void setType(Type type) {
        this.type = type.getId();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<EntityLogAttr> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<EntityLogAttr> attributes) {
        this.attributes = attributes;
    }
}
