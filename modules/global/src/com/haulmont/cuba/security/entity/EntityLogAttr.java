/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * Record containing changed entity attribute.
 * Created by <code>EntityLog</code> MBean.
 *
 * @author krivopustov
 * @version $Id$
 */
@MetaClass(name = "sec$EntityLogAttr")
@SystemLevel
public class EntityLogAttr extends AbstractNotPersistentEntity {

    private static final long serialVersionUID = 4258700403293876630L;

    public static final String VALUE_ID_SUFFIX = "-id";
    public static final String MP_SUFFIX = "-mp";

    @MetaProperty
    private EntityLogItem logItem;

    @MetaProperty
    private String name;

    @MetaProperty
    private String value;

    @MetaProperty
    private UUID valueId;

    @MetaProperty
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
        com.haulmont.chile.core.model.MetaClass metaClass = getClassFromEntityName(entityName);
        if (metaClass != null) {
            com.haulmont.chile.core.model.MetaProperty property = metaClass.getProperty(getName());
            if (property != null) {
                if (property.getRange().isDatatype()) {
                    return getValue();
                } else if (property.getRange().isEnum()) {
                    String nameKey = property.getRange().asEnumeration().getJavaClass().getSimpleName() + "." + getValue();
                    Messages messages = AppBeans.get(Messages.NAME);
                    return messages.getMessage(entityName.substring(0, entityName.lastIndexOf(".")), nameKey);
                } else {
                    return getValue();
                }
            } else {
                return getValue();
            }
        } else {
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
        String message;
        com.haulmont.chile.core.model.MetaClass metaClass = getClassFromEntityName(entityName);
        if (metaClass != null) {
            Messages messages = AppBeans.get(Messages.NAME);
            message = messages.getTools().getPropertyCaption(metaClass, getName());
        } else {
            return getName();
        }
        return (message != null ? message : getName());
    }

    private com.haulmont.chile.core.model.MetaClass getClassFromEntityName(String entityName) {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        com.haulmont.chile.core.model.MetaClass metaClass = metadata.getSession().getClass(entityName);
        return metaClass == null ? null : metadata.getExtendedEntities().getEffectiveMetaClass(metaClass);
    }

    @MetaProperty
    public String getLocValue() {
        if (!StringUtils.isBlank(messagesPack)) {
            Messages messages = AppBeans.get(Messages.NAME);
            return messages.getMessage(messagesPack, value);
        } else {
            return value;
        }
    }
}
