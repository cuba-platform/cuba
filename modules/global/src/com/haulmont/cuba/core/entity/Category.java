/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;
import java.util.List;

/**
 * @author devyatkin
 * @version $Id$
 */
@javax.persistence.Entity(name = "sys$Category")
@Table(name = "SYS_CATEGORY")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
@NamePattern("%s|name")
@SystemLevel
public class Category extends StandardEntity {

    private static final long serialVersionUID = 7160259865207148541L;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "ENTITY_TYPE", nullable = false)
    protected String entityType;

    @Column(name = "IS_DEFAULT")
    protected Boolean isDefault;

    @OneToMany(mappedBy = "category", targetEntity = CategoryAttribute.class)
    @OnDelete(com.haulmont.cuba.core.global.DeletePolicy.CASCADE)
    @OrderBy("orderNo")
    @Composition
    protected List<CategoryAttribute> categoryAttrs;

    @Column(name = "SPECIAL")
    protected String special;

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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
}