/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.Aggregation;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.sys.SetValueEntity;
import org.apache.openjpa.persistence.Persistent;
import org.springframework.core.convert.support.PropertyTypeDescriptor;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name="sys$CategoryAttribute")
@Table(name = "SYS_CATEGORY_ATTR")
@NamePattern("%s|name")
public class CategoryAttribute extends StandardEntity {

    private static final long serialVersionUID = -6959392628534815752L;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Column(name="NAME")
    private String name;

    @Column(name="DEFAULT_STRING")
    private String defaultString;

    @Column(name="DEFAULT_INT")
    private Integer defaultInt;

    @Column(name="DEFAULT_DOUBLE")
    private Double defaultDouble;

    @Column(name="DEFAULT_BOOLEAN")
    private Boolean defaultBoolean;

    @Column(name = "DEFAULT_DATE")
    private Date defaultDate;

    @Column(name="ENUMERATION")
    private String enumeration;

    @Column(name="DATA_TYPE")
    private String dataType;

    @Column(name="IS_ENTITY")
    private Boolean isEntity;

    @Column(name="DEFAULT_ENTITY_VALUE")
    @Persistent
    private UUID defaultEntityId;

    public void setCategory(Category entityType) {
        this.category = entityType;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnumeration(){
        return enumeration;
    }

    public void setEnumeration(String e){
        this.enumeration=e;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getIsEntity() {
        return isEntity;
    }

    public void setIsEntity(Boolean isEntity) {
        this.isEntity = isEntity;
    }

    public UUID getDefaultEntityId() {
        return defaultEntityId;
    }

    public void setDefaultEntityId(UUID defaultEntityId) {
        this.defaultEntityId = defaultEntityId;
    }

    public String getDefaultString() {
        return defaultString;
    }

    public void setDefaultString(String defaultString) {
        this.defaultString = defaultString;
    }

    public Integer getDefaultInt() {
        return defaultInt;
    }

    public void setDefaultInt(Integer defaultInt) {
        this.defaultInt = defaultInt;
    }

    public Double getDefaultDouble() {
        return defaultDouble;
    }

    public void setDefaultDouble(Double defaultDouble) {
        this.defaultDouble = defaultDouble;
    }

    public Boolean getDefaultBoolean() {
        return defaultBoolean;
    }

    public void setDefaultBoolean(Boolean defaultBoolean) {
        this.defaultBoolean = defaultBoolean;
    }

    public Date getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
    }


    public Object getDefaultValue() {
        if (defaultInt != null)
            return defaultInt;
        else if (defaultDouble != null)
            return defaultDouble;
        else if (defaultBoolean != null) return defaultBoolean;
        else if (defaultDate != null) return defaultDate;
        else if (defaultString != null) return defaultString;
        else if (defaultEntityId != null) return defaultEntityId;
        else return null;
    }
}
