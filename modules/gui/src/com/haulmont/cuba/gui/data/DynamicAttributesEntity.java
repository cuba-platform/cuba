/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.sys.SetValueEntity;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * The entity, that contains a set of dynamic attributes.
 *
 * @author devyatkin
 * @version $Id$
 */
public class DynamicAttributesEntity implements BaseEntity {

    private static final long serialVersionUID = -8091230910619941201L;

    protected MetaClass metaClass;
    protected UUID id;
    protected Map<String, Object> changed = new HashMap<>();
    protected Set<ValueListener> listeners = new LinkedHashSet<>();

    protected Map<String, CategoryAttributeValue> categoryValues = new HashMap<>();
    protected Map<String, Object> values = new HashMap<>();

    public DynamicAttributesEntity(MetaClass metaClass) {
        this.metaClass = metaClass;
        this.id = UuidProvider.createUuid();
    }

    public void addAttributeValue(CategoryAttribute attribute, CategoryAttributeValue categoryAttributeValue, Object value) {
        String attributeCode = DynamicAttributesUtils.encodeAttributeCode(attribute.getCode());
        categoryValues.put(attributeCode, categoryAttributeValue);
        values.put(attributeCode, value);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isDetached() {
        return false;
    }

    @Override
    public void setDetached(boolean detached) {
    }

    @Override
    public UUID getUuid() {
        return id;
    }

    @Override
    public Date getCreateTs() {
        return null;
    }

    @Override
    public void setCreateTs(Date date) {
    }

    @Override
    public String getCreatedBy() {
        return null;
    }

    @Override
    public void setCreatedBy(String createdBy) {
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Override
    public String getInstanceName() {
        return null;
    }

    @Override
    public void addListener(com.haulmont.chile.core.common.ValueListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        listeners.clear();
    }

    @Override
    public <T> T getValue(String name) {
        return (T) values.get(name);
    }

    @Override
    public void setValue(String name, Object value) {
        Object oldValue = values.get(name);
        if (!ObjectUtils.equals(oldValue, value)) {
            values.put(name, value);
            changed.put(name, value);
            CategoryAttributeValue categoryValue = categoryValues.get(name);
            if (value != null) {
                if (BaseEntity.class.isAssignableFrom(value.getClass())) {
                    categoryValue.setEntityValue(((Entity) value).getUuid());
                } else {
                    setValue(categoryValue, value);
                }
            } else {
                setValue(categoryValue, null);
            }

            for (ValueListener listener : listeners) {
                listener.propertyChanged(this, name, oldValue, value);
            }
        }
    }

    @Override
    public <T> T getValueEx(String propertyPath) {
        return (T) values.get(propertyPath);
    }

    @Override
    public void setValueEx(String propertyPath, Object value) {
        Object oldValue = values.get(propertyPath);
        if (!ObjectUtils.equals(oldValue, value)) {
            values.put(propertyPath, value);
            changed.put(propertyPath, value);
            CategoryAttributeValue attrValue = categoryValues.get(propertyPath);
            if (value != null) {
                if (Entity.class.isAssignableFrom(value.getClass())) {
                    attrValue.setEntityValue(((Entity) value).getUuid());
                } else {
                    setValue(attrValue, value);
                }
            } else {
                setValue(attrValue, null);
            }

            for (ValueListener listener : listeners) {
                listener.propertyChanged(this, propertyPath, oldValue, value);
            }
        }
    }

    public CategoryAttributeValue getCategoryValue(String name) {
        return categoryValues.get(name);
    }

    private void setValue(CategoryAttributeValue attrValue, Object value) {
        if (attrValue.getCategoryAttribute().getIsEntity()) {
            attrValue.setEntityValue((UUID) value);
        } else {
            String dataType = attrValue.getCategoryAttribute().getDataType();
            switch (PropertyType.valueOf(dataType)) {
                case INTEGER:
                    attrValue.setIntValue((Integer) value);
                    break;
                case DOUBLE:
                    attrValue.setDoubleValue((Double) value);
                    break;
                case BOOLEAN:
                    attrValue.setBooleanValue((Boolean) value);
                    break;
                case DATE:
                    attrValue.setDateValue((Date) value);
                    break;
                case STRING:
                    attrValue.setStringValue(StringUtils.trimToNull((String) value));
                    break;
                case ENUMERATION:
                    if (value != null)
                        attrValue.setStringValue(StringUtils.trimToNull(((SetValueEntity) value).getValue()));
                    else attrValue.setStringValue(null);
                    break;
                case ENTITY:
                    attrValue.setEntityValue((UUID) value);
                    break;
            }
        }
    }

    public void updateAttributeValue(CategoryAttributeValue attributeValue) {
        CategoryAttribute attribute = attributeValue.getCategoryAttribute();
        String attributeCode = DynamicAttributesUtils.encodeAttributeCode(attribute.getCode());
        categoryValues.put(attributeCode, attributeValue);
    }
}