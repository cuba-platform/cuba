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
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.model.MetaClass;

import javax.persistence.*;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.Persistent;

/**
 * Record containing changed entity attribute
 * Created by {@link com.haulmont.cuba.security.app.EntityLog} MBean
 */
@Entity(name = "sec$EntityLogAttr")
@Table(name = "SEC_ENTITY_LOG_ATTR")
public class EntityLogAttr extends BaseUuidEntity {
    private static final long serialVersionUID = 4258700403293876630L;

    public static final int VALUE_LEN = 1500;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private EntityLogItem logItem;

    @Column(name = "NAME", length = 50)
    private String name;

    @Column(name = "VALUE", length = VALUE_LEN)
    private String value;

    @Column(name = "VALUE_ID")
    @Persistent
    private UUID valueId;

    @Column(name = "MESSAGES_PACK", length = 200)
    private String messagesPack;

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

    @MetaProperty
    public String getDisplayValue() {
        if (StringUtils.isEmpty(getValue())) {
            return getValue();
        }
        final String entityName = getLogItem().getEntity();
        try {
            Class<?> aClass = Class.forName(entityName);
            MetaClass metaClass = MetadataProvider.getSession().getClass(aClass);
            com.haulmont.chile.core.model.MetaProperty property = metaClass.getProperty(getName());
            if (property != null) {
                if (property.getRange().isDatatype()) {
                    return getValue();
                } else if (property.getRange().isEnum()) {
                    String nameKey = property.getRange().asEnumeration().getJavaClass().getSimpleName() + "." + getValue();
                    return MessageProvider.getMessage(entityName.substring(0, entityName.lastIndexOf(".")), nameKey);
                } else {
                    return getValue();
                }
            } else {
                return getValue();
            }
        } catch (ClassNotFoundException e) {
            return getValue();
        }
    }

    public UUID getValueId() {
        return valueId;
    }

    public void setValueId(UUID valueId) {
        this.valueId = valueId;
    }

    public String getMessagesPack() {
        return messagesPack;
    }

    public void setMessagesPack(String messagesPack) {
        this.messagesPack = messagesPack;
    }

    @MetaProperty
    public String getDisplayName() {
        final String entityName = getLogItem().getEntity();
        final String message = MessageProvider.getMessage(entityName.substring(0, entityName.lastIndexOf(".")),
                entityName.substring(entityName.lastIndexOf(".") + 1, entityName.length()) + "." + getName());
        return message == null || message.contains(getClass().getSimpleName()) ? getName() : message;
    }

    @MetaProperty
    public String getLocValue() {
        if (!StringUtils.isBlank(messagesPack)) {
            return MessageProvider.getMessage(messagesPack, value);
        } else {
            return value;
        }
    }
}
