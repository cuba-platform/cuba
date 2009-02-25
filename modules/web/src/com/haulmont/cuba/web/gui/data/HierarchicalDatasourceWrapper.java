/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.01.2009 13:26:42
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.entity.Entity;
import com.itmill.toolkit.data.Container;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class HierarchicalDatasourceWrapper
    extends
        CollectionDatasourceWrapper
    implements
        Container.Hierarchical
{
    private String parentPropertyName;

    public HierarchicalDatasourceWrapper(CollectionDatasource datasource, String parentProperty)
    {
        super(datasource);
        this.parentPropertyName = parentProperty;
    }

    public Collection getChildren(Object itemId) {
        Set<Object> res = new HashSet<Object>();

        final Entity item = datasource.getItem(itemId);
        if (item == null) return Collections.emptyList();

        Collection<Object> ids = datasource.getItemIds();
        for (Object id : ids) {
            Entity currentItem = datasource.getItem(id);
            Object parentItem = ((Instance) currentItem).getValue(parentPropertyName);
            if (parentItem != null && parentItem.equals(item))
                res.add(currentItem.getId());
        }

        return res;
    }

    public Object getParent(Object itemId) {
        Instance item = (Instance) datasource.getItem(itemId);
        if (item == null)
            return null;
        else {
            Entity value = item.getValue(parentPropertyName);
            return value == null ? null : value.getId();
        }
    }

    public Collection rootItemIds() {
        Set<Object> result = new HashSet<Object>();
        Collection ids = datasource.getItemIds();

        for (Object id : ids) {
            Entity item = datasource.getItem(id);
            Object value = ((Instance) item).getValue(parentPropertyName);
            if (value == null) result.add(item.getId());
        }

        return result;
    }

    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        Instance item = (Instance) datasource.getItem(itemId);
        if (item != null) {
            item.setValue(parentPropertyName, datasource.getItem(newParentId));
            return true;
        }
        return false;
    }

    public boolean areChildrenAllowed(Object itemId) {
        return true;
    }

    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        return true;
    }

    public boolean isRoot(Object itemId) {
        Instance item = (Instance) datasource.getItem(itemId);
        return item != null && item.getValue(parentPropertyName) == null;
    }

    public boolean hasChildren(Object itemId) {
        final Entity item = datasource.getItem(itemId);
        if (item == null) return false;

        Collection<Object> ids = datasource.getItemIds();
        for (Object id : ids) {
            Entity currentItem = datasource.getItem(id);
            Object parentItem = ((Instance) currentItem).getValue(parentPropertyName);
            if (parentItem != null && parentItem.equals(item))
                return true;
        }

        return false;
    }
}
