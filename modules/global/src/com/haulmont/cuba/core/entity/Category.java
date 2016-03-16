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

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;
import java.util.List;

/**
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