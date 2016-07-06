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
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UuidProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Specific entity, delegating all calls to internal BaseGenericIdEntity.
 *
 * Obsolete. Will be removed in future releases.
 *
 */
public class DynamicAttributesEntity implements Entity {
    private static final long serialVersionUID = -8091230910619941201L;
    protected BaseGenericIdEntity mainItem;
    protected UUID id;
    protected Map<String, CategoryAttribute> attributesMap = new HashMap<>();

    public DynamicAttributesEntity(BaseGenericIdEntity mainItem, Collection<CategoryAttribute> attributes) {
        this.mainItem = mainItem;
        this.id = UuidProvider.createUuid();
        for (CategoryAttribute attribute : attributes) {
            attributesMap.put(attribute.getCode(), attribute);
        }
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public MetaClass getMetaClass() {
        return mainItem.getMetaClass();
    }

    @Override
    public String getInstanceName() {
        return null;
    }

    @Override
    public void addListener(com.haulmont.chile.core.common.ValueListener listener) {
        mainItem.addListener(listener);
    }

    @Override
    public void removeListener(ValueListener listener) {
        mainItem.removeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        mainItem.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        mainItem.removePropertyChangeListener(listener);
    }

    @Override
    public void removeAllListeners() {
        mainItem.removeAllListeners();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(String name) {
        return (T) mainItem.getValue(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(String name, Object value) {
        mainItem.setValue(name, value);

        //if we set an attribute from another type of entity, we need to set reference to CategoryAttribute manually
        //this is workaround to make #PL-5770 logic works with modern RuntimePropertiesDatasource
        String attributeCode = DynamicAttributesUtils.decodeAttributeCode(name);
        Map<String, CategoryAttributeValue> dynamicAttributes = mainItem.getDynamicAttributes();
        if (dynamicAttributes != null) {
            CategoryAttributeValue categoryAttributeValue = dynamicAttributes.get(attributeCode);
            if (categoryAttributeValue != null && categoryAttributeValue.getCategoryAttribute() == null) {
                categoryAttributeValue.setCategoryAttribute(attributesMap.get(attributeCode));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValueEx(String propertyPath) {
        return (T) mainItem.getValueEx(propertyPath);
    }

    @Override
    public void setValueEx(String propertyPath, Object value) {
        mainItem.setValueEx(propertyPath, value);
    }
}