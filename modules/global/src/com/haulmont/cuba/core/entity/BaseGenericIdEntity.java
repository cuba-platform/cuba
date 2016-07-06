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
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
 */
@MappedSuperclass
public abstract class BaseGenericIdEntity<T> extends AbstractInstance implements Entity<T> {

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

    public abstract void setId(T id);

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (__managed)
            __detached = true;
        out.defaultWriteObject();
    }

    @Override
    public MetaClass getMetaClass() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getSession().getClassNN(getClass());
    }

    @Override
    public void setValue(String property, Object newValue, boolean checkEquals) {
        if (this instanceof HasUuid && DynamicAttributesUtils.isDynamicAttribute(property)) {
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
                    categoryAttributeValue.setEntityId(((HasUuid) this).getUuid());
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

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        return Objects.equals(getId(), ((BaseGenericIdEntity) other).getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
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
        return getClass().getName() + "-" + getId() + " [" + state + "]";
    }
}