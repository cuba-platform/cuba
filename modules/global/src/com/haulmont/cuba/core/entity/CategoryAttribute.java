/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.Aggregation;
import com.haulmont.cuba.core.global.MessageUtils;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;
import java.util.UUID;

@Entity(name="sys$CategoryAttribute")
@Table(name = "SYS_CATEGORY_ATTR")
public class CategoryAttribute extends StandardEntity {

    private static final long serialVersionUID = -6959392628534815752L;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Column(name="NAME")
    private String name;

    @Column(name="DEFAULT_VALUE")
    private String defaultValue;

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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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
}
