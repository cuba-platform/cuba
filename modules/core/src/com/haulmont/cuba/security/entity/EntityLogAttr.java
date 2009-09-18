/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.05.2009 11:04:04
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.chile.core.annotations.MetaProperty;

import javax.persistence.*;

/**
 * Record containing changed entity attribute
 * Created by {@link com.haulmont.cuba.security.app.EntityLog} MBean
 */
@Entity(name = "sec$EntityLogAttr")
@Table(name = "SEC_ENTITY_LOG_ATTR")
public class EntityLogAttr extends BaseUuidEntity
{
    private static final long serialVersionUID = 4258700403293876630L;

    public static final int VALUE_LEN = 1500;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private EntityLogItem logItem;

    @Column(name = "NAME", length = 50)
    private String name;

    @Column(name = "VALUE", length = VALUE_LEN)
    private String value;

    @MetaProperty
    @Transient
    private String displayName;

    public EntityLogItem getLogItem() {
        return logItem;
    }

    public void setLogItem(EntityLogItem logItem) {
        this.logItem = logItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayName() {
        final String entityName = getLogItem().getEntity();
        final String message = MessageProvider.getMessage(entityName.substring(0, entityName.lastIndexOf(".")),
                entityName.substring(entityName.lastIndexOf(".") + 1, entityName.length()) + "." + getName());
        return message == null || message.contains(getClass().getSimpleName()) ? getName() : message;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
