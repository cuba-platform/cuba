/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.03.2009 10:38:29
 * $Id$
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import java.util.*;

public class HierarchicalDatasourceImpl<T extends Entity<K>, K>
    extends
        CollectionDatasourceImpl<T, K>
    implements
        HierarchicalDatasource<T, K>
{
    protected String hierarchyPropertyName;

    public HierarchicalDatasourceImpl(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        super(context, dataservice, id, metaClass, viewName);
    }

    public HierarchicalDatasourceImpl(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, String viewName, boolean softDeletion)
    {
        super(context, dataservice, id, metaClass, viewName);
        setSoftDeletion(softDeletion);
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

            List<Entity<K>> entities = new ArrayList<Entity<K>>();

            Collection<K> ids = getItemIds();
            for (K id : ids) {
                Entity<K> currentItem = getItem(id);
                Object parentItem = ((Instance) currentItem).getValue(hierarchyPropertyName);
                if (parentItem != null && parentItem.equals(item))
                    entities.add(currentItem);
            }

            if (sortInfos != null && sortInfos.length > 0) {
                MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
                Order order = sortInfos[0].getOrder();
                Collections.sort(entities, new EntityComparator(propertyPath, Order.ASC.equals(order)));
            }

            List<K> res = new ArrayList<K>();
            for (Entity<K> entity : entities) {
                res.add(entity.getId());
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
            Set<K> result = new HashSet<K>();
            for (K id : ids) {
                Entity<K> item = getItem(id);
                Object value = ((Instance) item).getValue(hierarchyPropertyName);
                if (value == null || !containsItem(getItemId((T) value))) result.add(item.getId());
            }
            return result;
        } else {
            return new HashSet<K>(ids);
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
