/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.*;

import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */

public class RuntimePropertiesMetaClass implements MetaClass {

    private Map<String, MetaProperty> properties = new HashMap<String, MetaProperty>();

    public void addProperty(MetaProperty property) {
        properties.put(property.getName(), property);
    }

    public void setProperties(List<MetaProperty> properties) {
        for (MetaProperty property : properties) {
            this.properties.put(property.getName(), property);
        }
    }

    public MetaModel getModel() {
        return null;
    }

    public Class getJavaClass() {
        return null;
    }

    public MetaProperty getProperty(String name) {
       return properties.get(name);
    }

    public MetaPropertyPath getPropertyEx(String propertyPath) {
        return new MetaPropertyPath(this, properties.get(propertyPath));
    }

    public MetaPropertyPath getPropertyPath(String propertyPath) {
        String[] properties = propertyPath.split("[.]");
        List<MetaProperty> metaProperties = new ArrayList<MetaProperty>();

		MetaProperty currentProperty;
		MetaClass currentClass = this;

		for (String property : properties) {
			if (currentClass == null) return null;
			currentProperty = currentClass.getProperty(property);
			if (currentProperty == null) return null;

			final Range range = currentProperty.getRange();
			currentClass = range.isClass() ? range.asClass() : null;

            metaProperties.add(currentProperty);
		}

		return new MetaPropertyPath(this, metaProperties.toArray(new MetaProperty[metaProperties.size()]));
    }

    public Collection<MetaProperty> getOwnProperties() {
        return null;
    }

    public Collection<MetaProperty> getProperties() {
        return properties.values();
    }

    public <T> T createInstance() throws InstantiationException, IllegalAccessException {
        return null;
    }

    public MetaClass getAncestor() {
        return null;
    }

    public Collection<MetaClass> getAncestors() {
        return null;
    }

    public Collection<MetaClass> getDescendants() {
        return null;
    }

    public String getName() {
        return null;
    }

    public String getFullName() {
        return null;
    }

    public String getCaption() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public UUID getUUID() {
        return null;
    }

    public Map<String, Object> getAnnotations() {
        return null;
    }
}
