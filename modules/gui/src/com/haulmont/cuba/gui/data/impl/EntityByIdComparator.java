/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * @author gorodnov
 * @version $Id$
 */
public class EntityByIdComparator<T extends Entity<K>, K> extends AbstractComparator<K> {
    private MetaPropertyPath propertyPath;
    private MetaProperty property;
    private CollectionDatasource datasource;

    public EntityByIdComparator(MetaPropertyPath propertyPath, CollectionDatasource<T, K> datasource, boolean asc) {
        super(asc);
        this.propertyPath = propertyPath;
        if (propertyPath.getMetaProperties().length == 1) {
            property = this.propertyPath.getMetaProperty();
        }
        this.datasource = datasource;
    }

    @Override
    public int compare(K key1, K key2) {
        Object o1 = getValue(datasource.getItem(key1));
        Object o2 = getValue(datasource.getItem(key2));

        return __compare(o1, o2);
    }

    private Object getValue(Instance instance) {
        Object value;
        if (property != null) {
            value = instance.getValue(property.getName());
        } else {
            value = instance.getValueEx(propertyPath.toString());
        }

        if (!(value == null || value instanceof Comparable)) {
            value = value.toString();
        }

        return value;
    }
}