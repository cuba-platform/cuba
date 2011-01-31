/*
* Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
* Haulmont Technology proprietary and confidential.
* Use is subject to license terms.

* Author: Dmitry Odobescu
* Created: 28.01.2011 18:30:36
*
* $Id$
*/
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import java.util.*;

public class HierarchicalPropertyDatasourceImpl <T extends Entity<K>, K> extends CollectionPropertyDatasourceImpl<T, K> implements HierarchicalDatasource<T, K> {
    protected String hierarchyPropertyName;

    public HierarchicalPropertyDatasourceImpl(String id, Datasource<Entity> ds, String property) {
        super(id, ds, property);
    }

    public String getHierarchyPropertyName() {
        return hierarchyPropertyName;
    }

    public void setHierarchyPropertyName(String hierarchyPropertyName) {
        this.hierarchyPropertyName = hierarchyPropertyName;
    }

    public Collection<K> getChildren(K itemId) {
        if (hierarchyPropertyName != null) {
            final Entity item = getItem(itemId);
            if (item == null)
                return Collections.emptyList();

            List<K> res = new ArrayList<K>();

            Collection<K> ids = getItemIds();
            for (K id : ids) {
                Entity<K> currentItem = getItem(id);
                Object parentItem = ((Instance) currentItem).getValue(hierarchyPropertyName);
                if (parentItem != null && parentItem.equals(item))
                    res.add(currentItem.getId());
            }

            return res;
        }
        return Collections.emptyList();
    }

    public K getParent(K itemId) {
        if (hierarchyPropertyName != null) {
            Instance item = (Instance) getItem(itemId);
            if (item == null)
                return null;
            else {
                Entity<K> value = item.getValue(hierarchyPropertyName);
                return value == null ? null : value.getId();
            }
        }
        return null;
    }

    public Collection<K> getRootItemIds() {
        Collection<K> ids = getItemIds();

        if (hierarchyPropertyName != null) {
            Set<K> result = new LinkedHashSet<K>();
            for (K id : ids) {
                Entity<K> item = getItem(id);
                Object value = ((Instance) item).getValue(hierarchyPropertyName);
                if (value == null || !containsItem(getItemId((T) value))) result.add(item.getId());
            }
            return result;
        } else {
            return new LinkedHashSet<K>(ids);
        }
    }

    public boolean isRoot(K itemId) {
        Instance item = (Instance) getItem(itemId);
        if (item == null) return false;

        if (hierarchyPropertyName != null) {
            Object value = item.getValue(hierarchyPropertyName);
            return (value == null || !containsItem(getItemId((T) value)));
        } else {
            return true;
        }
    }

    public boolean hasChildren(K itemId) {
        final Entity item = getItem(itemId);
        if (item == null) return false;

        if (hierarchyPropertyName != null) {
            Collection<K> ids = getItemIds();
            for (K id : ids) {
                Entity currentItem = getItem(id);
                Object parentItem = ((Instance) currentItem).getValue(hierarchyPropertyName);
                if (parentItem != null && parentItem.equals(item))
                    return true;
            }
        }

        return false;
    }

    public boolean canHasChildren(K itemId) {
        return true;
    }
}

