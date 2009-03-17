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

@Entity(name = "sec$EntityLog")
@Table(name = "SEC_ENTITY_LOG")
public class EntityLogItem extends BaseUuidEntity
{
    private static final long serialVersionUID = 5859030306889056606L;

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

    @Column(name = "ENTITY", length = 50)
    private String entity;

    @Column(name = "ATTR", length = 50)
    private String attribute;

    @Column(name = "VALUE", length = 500)
    private String value;

    @Column(name = "OLD_VALUE", length = 500)
    private String oldValue;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Date getEventTs() {
        return eventTs;
    }

    public void setEventTs(Date eventTs) {
        this.eventTs = eventTs;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
