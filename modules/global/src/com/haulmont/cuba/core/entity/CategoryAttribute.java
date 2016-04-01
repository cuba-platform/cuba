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
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.*;

/**
 */
@Entity(name = "sys$CategoryAttribute")
@Table(name = "SYS_CATEGORY_ATTR")
@NamePattern("%s|name")
@SystemLevel
@Listeners("report_CategoryAttributeListener")
public class CategoryAttribute extends StandardEntity {

    private static final long serialVersionUID = -6959392628534815752L;

    public static final int NAME_FIELD_LENGTH = 255;
    public static final int CODE_FIELD_LENGTH = 50;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Column(name = "CATEGORY_ENTITY_TYPE")
    private String categoryEntityType;

    @Column(name = "NAME", length = NAME_FIELD_LENGTH, nullable = false)
    private String name;

    @Column(name = "CODE", length = CODE_FIELD_LENGTH, nullable = false)
    private String code;

    @Column(name = "ENUMERATION")
    private String enumeration;

    @Column(name = "DATA_TYPE")
    private String dataType;

    @Column(name = "ENTITY_CLASS")
    private String entityClass;

    @Column(name = "DEFAULT_ENTITY_VALUE")
    private UUID defaultEntityId;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

    @Column(name = "SCREEN")
    private String screen;

    @Column(name = "REQUIRED")
    private Boolean required = false;

    @Column(name = "LOOKUP")
    private Boolean lookup = false;

    @Column(name = "TARGET_SCREENS")
    private String targetScreens;//comma separated list of screenId#componentId pairs. componentId might be empty

    @Column(name = "DEFAULT_STRING")
    private String defaultString;

    @Column(name = "DEFAULT_INT")
    private Integer defaultInt;

    @Column(name = "DEFAULT_DOUBLE")
    private Double defaultDouble;

    @Column(name = "DEFAULT_BOOLEAN")
    private Boolean defaultBoolean;

    @Column(name = "DEFAULT_DATE")
    private Date defaultDate;

    @Column(name = "DEFAULT_DATE_IS_CURRENT")
    private Boolean defaultDateIsCurrent;

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

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String e) {
        this.enumeration = e;
    }

    public PropertyType getDataType() {
        if (dataType == null) return null;

        return PropertyType.valueOf(dataType);
    }

    public void setDataType(PropertyType dataType) {
        this.dataType = dataType != null ? dataType.name() : null;
    }

    public Boolean getIsEntity() {
        return getDataType() == PropertyType.ENTITY;
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
        if (dataType != null) {
            switch (PropertyType.valueOf(dataType)) {
                case INTEGER: return defaultInt;
                case DOUBLE: return defaultDouble;
                case BOOLEAN: return defaultBoolean;
                case DATE: return defaultDate;
                case STRING: return defaultString;
                case ENUMERATION: return defaultString;
                case ENTITY: return defaultEntityId;
                default: return null;
            }
        }

        return null;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getLookup() {
        return lookup;
    }

    public void setLookup(Boolean lookup) {
        this.lookup = lookup;
    }

    public Boolean getDefaultDateIsCurrent() {
        return defaultDateIsCurrent;
    }

    public void setDefaultDateIsCurrent(Boolean defaultDateIsCurrent) {
        this.defaultDateIsCurrent = defaultDateIsCurrent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTargetScreens() {
        return targetScreens;
    }

    public void setTargetScreens(String targetScreens) {
        this.targetScreens = targetScreens;
    }

    public String getCategoryEntityType() {
        return categoryEntityType;
    }

    public void setCategoryEntityType(String categoryEntityType) {
        this.categoryEntityType = categoryEntityType;
    }

    public Set<String> targetScreensSet() {
        if (StringUtils.isNotBlank(targetScreens)) {
            return new HashSet<>(Arrays.asList(targetScreens.split(",")));
        } else {
            return Collections.emptySet();
        }
    }

    public List<String> getEnumerationOptions() {
        Preconditions.checkState(getDataType() == PropertyType.ENUMERATION, "Only enumeration attributes have options");
        String enumeration = getEnumeration();
        String[] values = StringUtils.split(enumeration, ',');
        return values != null ? Arrays.asList(values) : Collections.<String>emptyList();
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    @Nullable
    public Class getJavaClassForEntity(){
        if (StringUtils.isNotBlank(entityClass)) {
            return ReflectionHelper.getClass(entityClass);
        } else {
            return null;
        }
    }
}