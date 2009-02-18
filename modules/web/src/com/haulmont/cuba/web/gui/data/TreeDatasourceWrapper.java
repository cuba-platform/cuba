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
import com.itmill.toolkit.data.Container;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TreeDatasourceWrapper
        extends CollectionDatasourceWrapper
        implements Container.Hierarchical
{
    private String parentPropertyName;

    public TreeDatasourceWrapper(CollectionDatasource datasource, String parentProperty)
    {
        super(datasource);
        this.parentPropertyName = parentProperty;
    }

    public Collection getChildren(Object itemId) {
        Set result = new HashSet();
        Collection ids = datasource.getItemIds();
        for (Object id : ids) {
            Instance instance = (Instance) datasource.getItem(id);
            Object value = instance.getValue(parentPropertyName);
            if (value != null && value.equals(datasource.getItem(itemId)))
                result.add(instance);
        }
        return result;
    }

    public Object getParent(Object itemId) {
        Instance instance = (Instance) datasource.getItem(itemId);
        if (instance == null)
            return null;
        else {
            Object value = instance.getValue(parentPropertyName);
            return value == null ? null : getItemWrapper(value);
        }
    }

    public Collection rootItemIds() {
        Set result = new HashSet();
        Collection ids = datasource.getItemIds();
        for (Object id : ids) {
            Instance instance = (Instance) datasource.getItem(id);
            Object value = instance.getValue(parentPropertyName);
            if (value == null)
                result.add(instance);
        }
        return result;
    }

    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        Instance instance = (Instance) datasource.getItem(itemId);
        if (instance != null) {
            instance.setValue(parentPropertyName, newParentId);
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
        Instance instance = (Instance) datasource.getItem(itemId);
        return instance != null && instance.getValue(parentPropertyName) == null;
    }

    public boolean hasChildren(Object itemId) {
        Collection ids = datasource.getItemIds();
        for (Object id : ids) {
            Instance instance = (Instance) datasource.getItem(id);
            Object value = instance.getValue(parentPropertyName);
            if (value != null && value.equals(datasource.getItem(itemId)))
                return true;
        }
        return false;
    }
}
