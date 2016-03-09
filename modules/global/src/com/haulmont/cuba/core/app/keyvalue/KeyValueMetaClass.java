/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.keyvalue;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.impl.MetadataObjectImpl;
import com.haulmont.cuba.core.entity.KeyValueEntity;

import javax.annotation.Nullable;
import java.util.*;

/**
 * MetaClass for {@link KeyValueEntity}.
 */
public class KeyValueMetaClass extends MetadataObjectImpl implements MetaClass {

    private Map<String, MetaProperty> properties = new LinkedHashMap<>();

    public void addProperty(MetaProperty property) {
        properties.put(property.getName(), property);
    }

    public void removeProperty(String propertyName) {
        properties.remove(propertyName);
    }

    @Nullable
    @Override
    public MetaClass getAncestor() {
        return null;
    }

    @Override
    public List<MetaClass> getAncestors() {
        return Collections.emptyList();
    }

    @Override
    public Collection<MetaClass> getDescendants() {
        return Collections.emptyList();
    }

    @Override
    public MetaModel getModel() {
        return null; // temporary metaclass
    }

    @Override
    public Class getJavaClass() {
        return KeyValueEntity.class;
    }

    @Override
    public MetaProperty getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public MetaProperty getPropertyNN(String name) {
        MetaProperty property = getProperty(name);
        if (property == null)
            throw new IllegalArgumentException("Property '" + name + "' not found in " + getName());
        return property;
    }

    @Override
    public MetaPropertyPath getPropertyPath(String propertyPath) {
        MetaProperty currentProperty;

        currentProperty = this.getProperty(propertyPath);
        if (currentProperty == null) return null;

        return new MetaPropertyPath(this, currentProperty);
    }

    @Override
    public Collection<MetaProperty> getOwnProperties() {
        return properties.values();
    }

    @Override
    public Collection<MetaProperty> getProperties() {
        return properties.values();
    }

    @Override
    public String toString() {
        return "KeyValueMetaClass{" +
                "properties=" + properties.keySet() +
                '}';
    }
}
