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

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.*;
import java.util.UUID;

/**
 * Record containing changed entity attribute.
 * Created by <code>EntityLog</code> MBean.
 */
@Entity(name = "sec$EntityLogAttr")
@Table(name = "SEC_ENTITY_LOG_ATTR")
@SystemLevel
// TODO Make this entity transient when #1358 is fixed
//@MetaClass(name = "sec$EntityLogAttr")
public class EntityLogAttr extends BaseUuidEntity {
    private static final long serialVersionUID = 4258700403293876630L;

    public static final String VALUE_ID_SUFFIX = "-id";
    public static final String MP_SUFFIX = "-mp";

    public static final int VALUE_LEN = 1500;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
//    @MetaProperty
    private EntityLogItem logItem;

    @Column(name = "NAME", length = 50)
//    @MetaProperty
    private String name;

    @Column(name = "VALUE", length = VALUE_LEN)
//    @MetaProperty
    private String value;

    @Column(name = "VALUE_ID")
    @Persistent
//    @MetaProperty
    private UUID valueId;

    @Column(name = "MESSAGES_PACK", length = 200)
//    @MetaProperty
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
            com.haulmont.chile.core.model.MetaClass metaClass = getClassFromEntityName(entityName);
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
        String entityName = getLogItem().getEntity();
        String message = null;
        try {
            MetaClass metaClass = getClassFromEntityName(entityName);
            Messages messages = AppBeans.get(Messages.class);
            message = messages.getTools().getPropertyCaption(metaClass, getName());
        } catch (ClassNotFoundException e) {
            // if entityClass not found
            return getName();
        }
        return (message != null ? message : getName());
    }

    private com.haulmont.chile.core.model.MetaClass getClassFromEntityName(String entityName) throws ClassNotFoundException {
        Class<?> entityClass = ReflectionHelper.loadClass(entityName);
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.getSession().getClass(entityClass);
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
