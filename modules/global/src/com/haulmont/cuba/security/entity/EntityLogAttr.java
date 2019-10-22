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
package com.haulmont.cuba.security.entity;

import com.google.common.base.Strings;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

/**
 * Record containing changed entity attribute.
 */
@MetaClass(name = "sec$EntityLogAttr")
@SystemLevel
public class EntityLogAttr extends BaseUuidEntity {

    private static final long serialVersionUID = 4258700403293876630L;

    public static final String VALUE_ID_SUFFIX = "-id";
    public static final String MP_SUFFIX = "-mp";
    public static final String OLD_VALUE_SUFFIX = "-oldVl";
    public static final String OLD_VALUE_ID_SUFFIX = "-oldVlId";

    @MetaProperty
    private EntityLogItem logItem;

    @MetaProperty
    private String name;

    @MetaProperty
    private String value;

    @MetaProperty
    private String oldValue;

    @MetaProperty
    private String valueId;

    @MetaProperty
    private String oldValueId;

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

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    @MetaProperty
    public String getDisplayValue() {
        return getDisplayValue(getValue());
    }

    @MetaProperty
    public String getDisplayOldValue() {
        return getDisplayValue(getOldValue());
    }

    protected String getDisplayValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        final String entityName = getLogItem().getEntity();
        com.haulmont.chile.core.model.MetaClass metaClass = getClassFromEntityName(entityName);
        if (metaClass != null) {
            com.haulmont.chile.core.model.MetaProperty property = metaClass.getProperty(getName());
            if (property != null) {
                if (property.getRange().isDatatype()) {
                    return value;
                } else if (property.getRange().isEnum() && EnumClass.class.isAssignableFrom(property.getJavaType())) {
                    Messages messages = AppBeans.get(Messages.NAME);
                    Enum en = getEnumById(property.getRange().asEnumeration().getValues(), value);
                    if (en != null) {
                        return messages.getMessage(en);
                    } else  {
                        String nameKey = property.getRange().asEnumeration().getJavaClass().getSimpleName() + "." + value;
                        String packageName = property.getRange().asEnumeration().getJavaClass().getPackage().getName();
                        return messages.getMessage(packageName, nameKey);
                    }
                } else {
                    return value;
                }
            } else {
                return value;
            }
        } else {
            return value;
        }
    }

    protected Enum getEnumById(List<Enum> enums, String id) {
        for (Enum e : enums) {
            if (e instanceof EnumClass) {
                Object enumId = ((EnumClass) e).getId();
                if (id.equals(String.valueOf(enumId))) {
                    return e;
                }
            }
        }
        return null;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getOldValueId() {
        return oldValueId;
    }

    public void setOldValueId(String oldValueId) {
        this.oldValueId = oldValueId;
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
        return getLocValue(value);
    }

    @MetaProperty
    public String getLocOldValue() {
        return getLocValue(oldValue);
    }

    public String getLocValue(String value) {
        if (Strings.isNullOrEmpty(value)) return value;

        String entityName = getLogItem().getEntity();
        com.haulmont.chile.core.model.MetaClass metaClass = getClassFromEntityName(entityName);
        if (metaClass != null) {
            com.haulmont.chile.core.model.MetaProperty property = metaClass.getProperty(name);
            if (property != null && property.getRange().isEnum()) {
                try {
                    Enum caller = Enum.valueOf((Class<Enum>) property.getJavaType(), value);
                    Messages messages = AppBeans.get(Messages.NAME);
                    return messages.getMessage(caller);
                } catch (IllegalArgumentException ignored) {}
            }
        }

        if (!StringUtils.isBlank(messagesPack)) {
            Messages messages = AppBeans.get(Messages.NAME);
            return messages.getMessage(messagesPack, value);
        } else {
            return value;
        }
    }
}