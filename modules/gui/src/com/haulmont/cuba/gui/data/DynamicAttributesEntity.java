/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.global.UuidProvider;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Specific entity, delegating all calls to internal BaseGenericIdEntity.
 *
 * Obsolete. Will be removed in future releases.
 *
 * @author devyatkin
 * @version $Id$
 */
public class DynamicAttributesEntity implements BaseEntity {
    private static final long serialVersionUID = -8091230910619941201L;
    protected BaseGenericIdEntity mainItem;
    protected UUID id;

    public DynamicAttributesEntity(BaseGenericIdEntity mainItem) {
        this.mainItem = mainItem;
        this.id = UuidProvider.createUuid();
    }

    @Override
    public UUID getId() {
        return id;
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
    public void setValue(String name, Object value) {
        mainItem.setValue(name, value);
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

    @SuppressWarnings("unchecked")
    public CategoryAttributeValue getCategoryValue(String name) {
        Map<String, CategoryAttributeValue> dynamicAttributes = mainItem.getDynamicAttributes();
        return dynamicAttributes != null ? dynamicAttributes.get(DynamicAttributesUtils.decodeAttributeCode(name)) : null;
    }
}