/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
@javax.persistence.Entity(name = "sys$CategoryAttributeValue")
@Table(name = "SYS_ATTR_VALUE")
@SystemLevel
public class CategoryAttributeValue extends StandardEntity {

    private static final long serialVersionUID = -2861790889151226985L;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ATTR_ID")
    private CategoryAttribute categoryAttribute;


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
    @Persistent
    private UUID entityId;

    @Column(name = "ENTITY_VALUE")
    @Persistent
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

    public Object getValue() {
        if (stringValue != null)
            return stringValue;
        else if (intValue != null)
            return intValue;
        else if (doubleValue != null)
            return doubleValue;
        else if (dateValue != null)
            return dateValue;
        else if (booleanValue != null)
            return booleanValue;
        else if (entityValue != null)
            return entityValue;
        return null;
    }

}
