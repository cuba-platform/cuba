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
import com.google.common.collect.Multimap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.TimeSource;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base class for persistent entities.
 * <p>When choosing a base class for your entity, consider more specific base classes defining the primary key type:
 * <ul>
 * <li>{@link BaseUuidEntity}</li>
 * <li>{@link BaseLongIdEntity}</li>
 * <li>{@link BaseIntegerIdEntity}</li>
 * <li>{@link BaseStringIdEntity}</li>
 * </ul>
 * or most commonly used {@link StandardEntity}.
 * </p>
 *
 */
@MappedSuperclass
public abstract class BaseGenericIdEntity<T> extends AbstractInstance implements BaseEntity<T> {

    private static final long serialVersionUID = -8400641366148656528L;

    @Transient
    protected boolean __new = true;

    @Transient
    protected boolean __detached;

    protected transient boolean __managed;

    @Transient
    protected boolean __removed;

    @Transient
    protected String[] __inaccessibleAttributes;

    @Transient
    protected transient Multimap<String, UUID> __filteredData = null;

    @Transient
    protected String[] __filteredAttributes;

    @Transient
    protected byte[] __securityToken;

    @Transient
    protected Map<String, CategoryAttributeValue> dynamicAttributes = null;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = LOGIN_FIELD_LEN)
    protected String createdBy;

    public abstract void setId(T id);

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (__managed)
            __detached = true;
        out.defaultWriteObject();
    }

    /**
     * INTERNAL.
     */
    public boolean __new() {
        return __new;
    }

    /**
     * INTERNAL.
     */
    public void __new(boolean cubaNew) {
        this.__new = cubaNew;
    }

    /**
     * INTERNAL.
     */
    public boolean __managed() {
        return __managed;
    }

    /**
     * INTERNAL.
     */
    public void __managed(boolean cubaManaged) {
        this.__managed = cubaManaged;
    }

    /**
     * INTERNAL.
     */
    public boolean __detached() {
        return __detached;
    }

    /**
     * INTERNAL.
     */
    public void __detached(boolean detached) {
        this.__detached = detached;
    }

    /**
     * INTERNAL.
     */
    public boolean __removed() {
        return __removed;
    }

    /**
     * INTERNAL.
     */
    public void __removed(boolean removed) {
        this.__removed = removed;
    }

    /**
     * INTERNAL
     */
    public String[] __inaccessibleAttributes() {
        return __inaccessibleAttributes;
    }

    /**
     * INTERNAL
     */
    public void __inaccessibleAttributes(String[] __inaccessibleAttributes) {
        this.__inaccessibleAttributes = __inaccessibleAttributes;
    }

    @Override
    public MetaClass getMetaClass() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getSession().getClassNN(getClass());
    }

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public void setValue(String property, Object newValue, boolean checkEquals) {
        if (DynamicAttributesUtils.isDynamicAttribute(property)) {
            Preconditions.checkState(dynamicAttributes != null, "Dynamic attributes should be loaded explicitly");
            String attributeCode = DynamicAttributesUtils.decodeAttributeCode(property);
            CategoryAttributeValue categoryAttributeValue = dynamicAttributes.get(attributeCode);
            Object oldValue = categoryAttributeValue != null ? categoryAttributeValue.getValue() : null;

            if (newValue == null) {
                if (categoryAttributeValue != null) {
                    categoryAttributeValue.setValue(null);
                    categoryAttributeValue.setDeleteTs(AppBeans.get(TimeSource.class).currentTimestamp());
                    propertyChanged(property, oldValue, null);
                }
            } else if (!ObjectUtils.equals(oldValue, newValue)) {
                if (categoryAttributeValue != null) {
                    categoryAttributeValue.setValue(newValue);
                    categoryAttributeValue.setDeleteTs(null);
                } else {
                    Metadata metadata = AppBeans.get(Metadata.NAME);

                    categoryAttributeValue = metadata.create(CategoryAttributeValue.class);
                    categoryAttributeValue.setValue(newValue);
                    categoryAttributeValue.setEntityId(getUuid());
                    categoryAttributeValue.setCode(attributeCode);
                    DynamicAttributes dynamicAttributesBean = AppBeans.get(DynamicAttributes.NAME);
                    categoryAttributeValue.setCategoryAttribute(
                            dynamicAttributesBean.getAttributeForMetaClass(getMetaClass(), attributeCode));
                    dynamicAttributes.put(attributeCode, categoryAttributeValue);
                }
                propertyChanged(property, null, newValue);
            }
        } else {
            super.setValue(property, newValue, checkEquals);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V getValue(String property) {
        if (DynamicAttributesUtils.isDynamicAttribute(property)) {
            if (PersistenceHelper.isNew(this) && dynamicAttributes == null) {
                dynamicAttributes = new HashMap<>();
            }

            Preconditions.checkState(dynamicAttributes != null, "Dynamic attributes should be loaded explicitly");
            CategoryAttributeValue categoryAttributeValue = dynamicAttributes.get(DynamicAttributesUtils.decodeAttributeCode(property));
            if (categoryAttributeValue != null) {
                return (V) categoryAttributeValue.getValue();
            } else {
                return null;
            }
        } else {
            return super.getValue(property);
        }
    }

    public void setDynamicAttributes(Map<String, CategoryAttributeValue> dynamicAttributes) {
        this.dynamicAttributes = dynamicAttributes;
    }

    @Nullable
    public Map<String, CategoryAttributeValue> getDynamicAttributes() {
        return dynamicAttributes;
    }

    /**
     * INTERNAL.
     */
    public Multimap<String, UUID> __filteredData() {
        return __filteredData;
    }

    /**
     * INTERNAL.
     */
    public void __filteredData(Multimap<String, UUID> filtered) {
        this.__filteredData = filtered;
    }

    /**
     * INTERNAL.
     */
    public byte[] __securityToken() {
        return __securityToken;
    }

    /**
     * INTERNAL.
     */
    public void __securityToken(byte[] securityToken) {
        this.__securityToken = securityToken;
    }

    /**
     * INTERNAL.
     */
    public String[] __filteredAttributes() {
        return __filteredAttributes;
    }

    /**
     * INTERNAL.
     */
    public void __filteredAttributes(String[] __filteredAttributes) {
        this.__filteredAttributes = __filteredAttributes;
    }

    @Override
    public String toString() {
        String state = "";
        if (__new)
            state += "new,";
        if (__managed)
            state += "managed,";
        if (__detached)
            state += "detached,";
        if (__removed)
            state += "removed,";
        if (state.length() > 0)
            state = state.substring(0, state.length() - 1);
        return super.toString() + " [" + state + "]";
    }
}