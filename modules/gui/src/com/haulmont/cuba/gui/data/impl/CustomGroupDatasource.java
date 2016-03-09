/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;

import java.util.Collection;
import java.util.Map;

/**
 * Base class for custom implementations of {@link GroupDatasource}.
 * <ul>
 * <li>In a subclass, implement the {@link #getEntities(Map)} method and return a collection of entities from it.
 * <li>Register your subclass in the {@code datasourceClass} attribute of the datasource XML element.
 * </ul>
 */
public abstract class CustomGroupDatasource<T extends Entity<K>, K>
        extends GroupDatasourceImpl<T, K> {

    /**
     * Callback method to be implemented in subclasses.
     * @param params    datasource parameters, as described in {@link CollectionDatasource#refresh(java.util.Map)}
     * @return          collection of entities to populate the datasource
     */
    protected abstract Collection<T> getEntities(Map<String, Object> params);

    @Override
    protected void loadData(Map<String, Object> params) {
        Collection<T> entities = getEntities(params);

        detachListener(data.values());
        data.clear();

        if (entities != null) {
            for (T entity : entities) {
                data.put(entity.getId(), entity);
                attachListener(entity);
            }
        }
    }
}
