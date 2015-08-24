/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

import com.google.common.base.Preconditions;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * @author devyatkin
 * @version $Id$
 */
@javax.persistence.Entity(name = "sys$CategoryAttributeValue")
@Table(name = "SYS_ATTR_VALUE")
@SystemLevel
public class CategoryAttributeValue extends StandardEntity {

    private static final long serialVersionUID = -2861790889151226985L;

    @ManyToOne
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
        } else if (value instanceof Entity) {
            setEntityValue(((Entity) value).getUuid());
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
            return AppBeans.get(DataManager.class)
                    .load(new LoadContext(categoryAttribute.getJavaClassForEntity())
                            .setSoftDeletion(false).setId(entityValue));
        }

        return null;
    }
}