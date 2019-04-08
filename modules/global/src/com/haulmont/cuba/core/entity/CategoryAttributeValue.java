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

import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.*;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@javax.persistence.Entity(name = "sys$CategoryAttributeValue")
@Table(name = "SYS_ATTR_VALUE")
@SystemLevel
public class CategoryAttributeValue extends StandardEntity {

    private static final long serialVersionUID = -2861790889151226985L;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ATTR_ID")
    private CategoryAttribute categoryAttribute;

    @Column(name = "CODE", nullable = false)
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

    @Column(name = "DATE_WO_TIME_VALUE")
    private LocalDate dateWithoutTimeValue;

    @Embedded
    @EmbeddedParameters(nullAllowed = false)
    private ReferenceToEntity entity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="entityId", column=@Column(name="ENTITY_VALUE")),
            @AttributeOverride(name="stringEntityId", column=@Column(name="STRING_ENTITY_VALUE")),
            @AttributeOverride(name="intEntityId", column=@Column(name="INT_ENTITY_VALUE")),
            @AttributeOverride(name="longEntityId", column=@Column(name="LONG_ENTITY_VALUE"))
    })
    @EmbeddedParameters(nullAllowed = false)
    private ReferenceToEntity entityValue;

    @Transient
    private BaseGenericIdEntity transientEntityValue;

    @OneToMany(mappedBy = "parent")
    @OnDelete(DeletePolicy.CASCADE)
    private List<CategoryAttributeValue> childValues;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private CategoryAttributeValue parent;

    @Transient
    private List<Object> transientCollectionValue;

    @PostConstruct
    public void init() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        entity = metadata.create(ReferenceToEntity.class);
        entityValue = metadata.create(ReferenceToEntity.class);
    }

    public void setCategoryAttribute(CategoryAttribute categoryAttribute) {
        this.categoryAttribute = categoryAttribute;
    }

    public CategoryAttribute getCategoryAttribute() {
        return categoryAttribute;
    }

    public ReferenceToEntity getEntity() {
        return entity;
    }

    public void setEntity(ReferenceToEntity entity) {
        this.entity = entity;
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

    public LocalDate getDateWithoutTimeValue() {
        return dateWithoutTimeValue;
    }

    public void setDateWithoutTimeValue(LocalDate dateWithoutTimeValue) {
        this.dateWithoutTimeValue = dateWithoutTimeValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ReferenceToEntity getEntityValue() {
        return entityValue;
    }

    public void setEntityValue(ReferenceToEntity entityValue) {
        this.entityValue = entityValue;
    }

    public BaseGenericIdEntity getTransientEntityValue() {
        return transientEntityValue;
    }

    public void setTransientEntityValue(BaseGenericIdEntity transientEntityValue) {
        this.transientEntityValue = transientEntityValue;
    }

    public List<CategoryAttributeValue> getChildValues() {
        return childValues;
    }

    public void setChildValues(List<CategoryAttributeValue> childValues) {
        this.childValues = childValues;
    }

    public CategoryAttributeValue getParent() {
        return parent;
    }

    public void setParent(CategoryAttributeValue parent) {
        this.parent = parent;
    }

    public List<Object> getTransientCollectionValue() {
        return transientCollectionValue;
    }

    public void setTransientCollectionValue(List<Object> transientCollectionValue) {
        this.transientCollectionValue = transientCollectionValue;
    }

    //todo eude support enumerations
    public void setValue(Object value) {
        if (value == null) {
            stringValue = null;
            intValue = null;
            doubleValue = null;
            booleanValue = null;
            dateValue = null;
            dateWithoutTimeValue = null;
            entityValue.setObjectEntityId(null);
            transientEntityValue = null;
            transientCollectionValue = null;
        } else if (value instanceof LocalDate) {
            setDateWithoutTimeValue((LocalDate) value);
        } else if (value instanceof Date) {
            setDateValue((Date) value);
        } else if (value instanceof Integer) {
            setIntValue((Integer) value);
        } else if (value instanceof Double) {
            setDoubleValue((Double) value);
        } else if (value instanceof Boolean) {
            setBooleanValue((Boolean) value);
        } else if (value instanceof BaseGenericIdEntity) {
            ReferenceToEntitySupport referenceToEntitySupport = AppBeans.get(ReferenceToEntitySupport.class);
            Object referenceId = referenceToEntitySupport.getReferenceId((BaseGenericIdEntity) value);
            entityValue.setObjectEntityId(referenceId);
            setTransientEntityValue((BaseGenericIdEntity) value);
        } else if (value instanceof String) {
            setStringValue((String) value);
        } else if (value instanceof List) {
            setTransientCollectionValue((List<Object>) value);
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
        } else if (dateWithoutTimeValue != null) {
            return dateWithoutTimeValue;
        } else if (booleanValue != null) {
            return booleanValue;
        } else if (transientEntityValue != null) {
            return transientEntityValue;
        } if (transientCollectionValue != null) {
            return transientCollectionValue;
        }

        return null;
    }

    public void setObjectEntityId(Object entityId) {
        entity.setObjectEntityId(entityId);
    }

    public Object getObjectEntityId() {
        return entity.getObjectEntityId();
    }

    public void setObjectEntityValueId(Object entityId) {
        entityValue.setObjectEntityId(entityId);
    }

    public Object getObjectEntityValueId() {
        return entityValue.getObjectEntityId();
    }
}