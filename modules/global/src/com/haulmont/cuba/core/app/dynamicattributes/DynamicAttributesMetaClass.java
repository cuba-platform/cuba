/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.impl.MetadataObjectImpl;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author devyatkin
 * @version $Id$
 */

public class DynamicAttributesMetaClass extends MetadataObjectImpl implements MetaClass {

    private Map<String, MetaProperty> properties = new LinkedHashMap<>();
    private Map<String, CategoryAttribute> attributes = new LinkedHashMap<>();

    public void addProperty(MetaProperty property, CategoryAttribute attribute) {
        properties.put(property.getName(), property);
        attributes.put(property.getName(), attribute);
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

    public Collection<MetaProperty> getPropertiesFilteredByCategory(final Category category) {
        return Collections2.filter(getProperties(), new Predicate<MetaProperty>() {
            @Override
            public boolean apply(@Nullable MetaProperty input) {
                if (input != null && category != null) {
                    CategoryAttribute categoryAttribute = attributes.get(input.getName());
                    return category.equals(categoryAttribute.getCategory());
                } else {
                    return false;
                }
            }
        });
    }
}
