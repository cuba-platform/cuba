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

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import java.util.*;

/**
 */
public class HierarchicalDatasourceImpl<T extends Entity<K>, K>
        extends CollectionDatasourceImpl<T, K>
        implements HierarchicalDatasource<T, K> {

    protected String hierarchyPropertyName;

    @Override
    public String getHierarchyPropertyName() {
        return hierarchyPropertyName;
    }

    @Override
    public void setHierarchyPropertyName(String hierarchyPropertyName) {
        this.hierarchyPropertyName = hierarchyPropertyName;
    }

    @Override
    public Collection<K> getChildren(K itemId) {
        if (hierarchyPropertyName != null) {
            final Entity item = getItem(itemId);
            if (item == null)
                return Collections.emptyList();

            List<K> res = new ArrayList<>();

            Collection<K> ids = getItemIds();
            for (K id : ids) {
                Entity<K> currentItem = getItem(id);
                Object parentItem = currentItem.getValue(hierarchyPropertyName);
                if (parentItem != null && parentItem.equals(item))
                    res.add(currentItem.getId());
            }

            return res;
        }
        return Collections.emptyList();
    }

    @Override
    public K getParent(K itemId) {
        if (hierarchyPropertyName != null) {
            Instance item = getItem(itemId);
            if (item == null)
                return null;
            else {
                Entity<K> value = item.getValue(hierarchyPropertyName);
                return value == null ? null : value.getId();
            }
        }
        return null;
    }

    @Override
    public Collection<K> getRootItemIds() {
        Collection<K> ids = getItemIds();

        if (hierarchyPropertyName != null) {
            Set<K> result = new LinkedHashSet<>();
            for (K id : ids) {
                Entity<K> item = getItemNN(id);
                Object value = item.getValue(hierarchyPropertyName);
                if (value == null || !containsItem(((T) value).getId()))
                    result.add(item.getId());
            }
            return result;
        } else {
            return new LinkedHashSet<>(ids);
        }
    }

    @Override
    public boolean isRoot(K itemId) {
        Instance item = getItem(itemId);
        if (item == null) return false;

        if (hierarchyPropertyName != null) {
            Object value = item.getValue(hierarchyPropertyName);
            return (value == null || !containsItem(((T) value).getId()));
        } else {
            return true;
        }
    }

    @Override
    public boolean hasChildren(K itemId) {
        final Entity item = getItem(itemId);
        if (item == null) return false;

        if (hierarchyPropertyName != null) {
            Collection<K> ids = getItemIds();
            for (K id : ids) {
                Entity currentItem = getItem(id);
                Object parentItem = currentItem.getValue(hierarchyPropertyName);
                if (parentItem != null && parentItem.equals(item))
                    return true;
            }
        }

        return false;
    }
}