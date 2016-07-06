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

import com.google.common.base.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@javax.persistence.Entity(name = "sys$CategoryAttributeValue")
@Table(name = "SYS_ATTR_VALUE")
@SystemLevel
public class CategoryAttributeValue extends StandardEntity {

    private static final long serialVersionUID = -2861790889151226985L;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ATTR_ID")
    private CategoryAttribute categoryAttribute;

    @Column(name = "CODE")
    private String code;

    @Column(name = "STRING_VALUE")
    private String stringValue;

    @Column(name = "INTEGER_VALUE")
    private Integer intValue;

    @Column(name = "DOUBLE_VALUE")
    private Double doubleValue;

    @Column(name = "BOOLEAN_VALUE")
    private Boolean booleanValue;

    @Column(name = "DATE_VALUE")
    private Date dateValue;

    @Column(name = "ENTITY_ID")
    private UUID entityId;

    @Column(name = "ENTITY_VALUE")
    private UUID entityValue;

    public void setCategoryAttribute(CategoryAttribute categoryAttribute) {
        this.categoryAttribute = categoryAttribute;
    }

    public CategoryAttribute getCategoryAttribute() {
        return categoryAttribute;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public UUID getEntityValue() {
        return entityValue;
    }

    public void setEntityValue(UUID entityValue) {
        this.entityValue = entityValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //todo eude support enumerations
    public void setValue(Object value) {
        if (value == null) {
            stringValue = null;
            intValue = null;
            doubleValue = null;
            booleanValue = null;
            entityValue = null;
            dateValue = null;
        } else if (value instanceof Date) {
            setDateValue((Date) value);
        } else if (value instanceof Integer) {
            setIntValue((Integer) value);
        } else if (value instanceof Double) {
            setDoubleValue((Double) value);
        } else if (value instanceof Boolean) {
            setBooleanValue((Boolean) value);
        } else if (value instanceof UUID) {
            setEntityValue((UUID) value);
        } else if (value instanceof HasUuid) {
            setEntityValue(((HasUuid) value).getUuid());
        } else if (value instanceof String) {
            setStringValue((String) value);
        } else {
            throw new IllegalArgumentException("Unsupported value type " + value.getClass());
        }
    }

    public Object getValue() {
        if (stringValue != null) {
            return stringValue;
        } else if (intValue != null) {
            return intValue;
        } else if (doubleValue != null) {
            return doubleValue;
        } else if (dateValue != null) {
            return dateValue;
        } else if (booleanValue != null) {
            return booleanValue;
        } else if (entityValue != null) {
            Preconditions.checkState(categoryAttribute != null, "Could not resolve entity value, " +
                    "because categoryAttribute is not loaded for attribute value " + id);
            Preconditions.checkState(StringUtils.isNotBlank(categoryAttribute.getEntityClass()),
                    "Could not resolve class by empty dataType. Attribute value " + id);
            Class javaClass = categoryAttribute.getJavaClassForEntity();
            Preconditions.checkState(javaClass != null,
                    "Could not resolve java class. Attribute value " + id);
            LoadContext loadContext =  LoadContext.create(javaClass)
                    .setView(View.MINIMAL)
                    .setSoftDeletion(false);
            if (BaseUuidEntity.class.isAssignableFrom(javaClass)) {
                loadContext.setId(entityValue);
            } else {
                Metadata metadata = AppBeans.get(Metadata.class);
                MetaClass metaClass = metadata.getClassNN(javaClass);
                loadContext.setQueryString(String.format("select e from %s e where e.uuid = :entityId", metaClass.getName()))
                        .setParameter("entityId", entityValue);
            }
            return AppBeans.get(DataManager.class).load(loadContext);
        }

        return null;
    }
}