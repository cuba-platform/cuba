/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.Aggregation;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;

import javax.persistence.*;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
@javax.persistence.Entity(name = "sys$Category")
@Table(name = "SYS_CATEGORY")
@NamePattern("%s|name")
public class Category extends StandardEntity {

    private static final long serialVersionUID = 7160259865207148541L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ENTITY_TYPE")
    private String entityType;

    @Column(name = "IS_DEFAULT")
    private Boolean isDefault;

    @OneToMany(mappedBy = "category", targetEntity = CategoryAttribute.class)
    @OnDelete(com.haulmont.cuba.core.global.DeletePolicy.CASCADE)
    @Aggregation
    private List<CategoryAttribute> categoryAttrs;

    @Column(name = "SPECIAL")
    private String special;

    public String getName() {
        return name;
    }

    public List<CategoryAttribute> getCategoryAttrs() {
        return categoryAttrs;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setCategoryAttrs(List<CategoryAttribute> categoryAttrs) {
        this.categoryAttrs = categoryAttrs;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Boolean getIsDefault(){
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault){
        this.isDefault=isDefault;
    }

    public String getSpecial(){
        return special;
    }

    public void setSpecial(String special){
        this.special=special;
    }
}
