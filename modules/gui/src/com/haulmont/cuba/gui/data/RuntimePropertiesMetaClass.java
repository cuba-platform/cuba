/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.*;
import com.haulmont.chile.core.model.impl.MetadataObjectImpl;

import java.util.*;

/**
 * @author devyatkin
 * @version $Id$
 */

public class RuntimePropertiesMetaClass extends MetadataObjectImpl<MetaClass> implements MetaClass {

    private Map<String, MetaProperty> properties = new LinkedHashMap<String, MetaProperty>();

    public void addProperty(MetaProperty property) {
        properties.put(property.getName(), property);
    }

    public void setProperties(List<MetaProperty> properties) {
        for (MetaProperty property : properties) {
            this.properties.put(property.getName(), property);
        }
    }

    @Override
    public MetaModel getModel() {
        return null; // temporary metaclass
    }

    @Override
    public Class getJavaClass() {
        throw new UnsupportedOperationException();
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
    public MetaPropertyPath getPropertyEx(String propertyPath) {
        return new MetaPropertyPath(this, properties.get(propertyPath));
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
    public <T> T createInstance() throws InstantiationException, IllegalAccessException {
        throw new UnsupportedOperationException();
    }
}
