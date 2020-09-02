/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.components.data.table;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.TreeTableItems;
import com.haulmont.cuba.gui.model.CollectionContainer;

import java.util.*;

public class ContainerTreeTableItems<E extends Entity>
            extends ContainerTableItems<E>
            implements TreeTableItems<E> {

    private final String hierarchyProperty;
    private final boolean showOrphans;

    public ContainerTreeTableItems(CollectionContainer<E> container, String hierarchyProperty, boolean showOrphans) {
        super(container);
        this.hierarchyProperty = hierarchyProperty;
        this.showOrphans = showOrphans;
    }

    public ContainerTreeTableItems(CollectionContainer<E> container, String hierarchyProperty) {
        this(container, hierarchyProperty, true);
    }

    @Override
    public String getHierarchyPropertyName() {
        return hierarchyProperty;
    }

    @Override
    public Collection<?> getRootItemIds() {
        Collection<?> ids = getItemIds();

        if (hierarchyProperty != null) {
            Set<Object> result = new LinkedHashSet<>();
            for (Object id : ids) {
                Entity item = getItemNN(id);
                Entity parentItem = item.getValue(hierarchyProperty);
                if (parentItem == null || (showOrphans && container.getItemOrNull(parentItem.getId()) == null))
                    result.add(item.getId());
            }
            return result;
        } else {
            return new LinkedHashSet<>(ids);
        }
    }

    @Override
    public Object getParent(Object itemId) {
        if (hierarchyProperty != null) {
            Instance item = getItem(itemId);
            if (item == null)
                return null;
            else {
                Entity parentItem = item.getValue(hierarchyProperty);
                return parentItem == null ? null : parentItem.getId();
            }
        }
        return null;
    }

    @Override
    public Collection<?> getChildren(Object itemId) {
        if (hierarchyProperty != null) {
            Entity currentItem = getItem(itemId);
            if (currentItem == null)
                return Collections.emptyList();

            List<Object> res = new ArrayList<>();

            Collection ids = getItemIds();
            for (Object id : ids) {
                Entity item = getItemNN(id);
                Entity parentItem = item.getValue(hierarchyProperty);
                if (parentItem != null && parentItem.getId().equals(itemId))
                    res.add(item.getId());
            }

            return res;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isRoot(Object itemId) {
        Instance item = getItem(itemId);
        if (item == null) return false;

        if (hierarchyProperty != null) {
            Entity parentItem = item.getValue(hierarchyProperty);
            return (parentItem == null || (showOrphans && container.getItemOrNull(parentItem.getId()) == null));
        } else {
            return true;
        }
    }

    @Override
    public boolean hasChildren(Object itemId) {
        Entity currentItem = getItem(itemId);
        if (currentItem == null)
            return false;

        if (hierarchyProperty != null) {
            Collection ids = getItemIds();
            for (Object id : ids) {
                Entity item = getItemNN(id);
                Entity parentItem = item.getValue(hierarchyProperty);
                if (parentItem != null && parentItem.getId().equals(itemId))
                    return true;
            }
        }

        return false;
    }

    @Override
    public Object firstItemId() {
        Collection<?> rootItemIds = getRootItemIds();
        return rootItemIds.isEmpty() ? null : rootItemIds.iterator().next();
    }
}
