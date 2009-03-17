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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HierarchicalDatasourceImpl<T extends Entity, K>
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

    public String getHierarchyPropertyName() {
        return hierarchyPropertyName;
    }

    public void setHierarchyPropertyName(String hierarchyPropertyName) {
        this.hierarchyPropertyName = hierarchyPropertyName;
    }

    public Collection<K> getChildren(K itemId) {
        Set<K> res = new HashSet<K>();

        final Entity item = getItem(itemId);
        if (item == null) return Collections.emptyList();

        Collection<K> ids = getItemIds();
        for (K id : ids) {
            Entity<K> currentItem = getItem(id);
            Object parentItem = ((Instance) currentItem).getValue(hierarchyPropertyName);
            if (parentItem != null && parentItem.equals(item))
                res.add((K) currentItem.getId());
        }

        return res;
    }

    public K getParent(K itemId) {
        Instance item = (Instance) getItem(itemId);
        if (item == null)
            return null;
        else {
            Entity<K> value = item.getValue(hierarchyPropertyName);
            return value == null ? null : value.getId();
        }
    }

    public Collection<K> getRootItemIds() {
        Set<K> result = new HashSet<K>();
        Collection<K> ids = getItemIds();

        for (K id : ids) {
            Entity<K> item = getItem(id);
            Object value = ((Instance) item).getValue(hierarchyPropertyName);
            if (value == null) result.add(item.getId());
        }

        return result;
    }

    public boolean isRoot(K itemId) {
        Instance item = (Instance) getItem(itemId);
        return item != null && item.getValue(hierarchyPropertyName) == null;
    }

    public boolean hasChildren(K itemId) {
        final Entity item = getItem(itemId);
        if (item == null) return false;

        Collection<K> ids = getItemIds();
        for (K id : ids) {
            Entity currentItem = getItem(id);
            Object parentItem = ((Instance) currentItem).getValue(hierarchyPropertyName);
            if (parentItem != null && parentItem.equals(item))
                return true;
        }

        return false;
    }
}
