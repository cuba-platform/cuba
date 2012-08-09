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
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.vaadin.data.Container;

import java.util.Collection;

public class HierarchicalDsWrapper
    extends
        CollectionDsWrapper
    implements
        Container.Hierarchical
{
    private String parentPropertyName;

    public HierarchicalDsWrapper(HierarchicalDatasource datasource)
    {
        super(datasource);
        this.parentPropertyName = datasource.getHierarchyPropertyName();
    }

    public HierarchicalDsWrapper(HierarchicalDatasource datasource, Collection<MetaPropertyPath> properties)
    {
        super(datasource, properties);
        this.parentPropertyName = datasource.getHierarchyPropertyName();
    }

    public Collection getChildren(Object itemId) {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).getChildren(itemId);
    }

    public Object getParent(Object itemId) {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).getParent(itemId);
    }

    public Collection rootItemIds() {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).getRootItemIds();
    }

    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        Instance item = datasource.getItem(itemId);
        if (item != null) {
            item.setValue(parentPropertyName, datasource.getItem(newParentId));
            return true;
        }
        return false;
    }

    public boolean areChildrenAllowed(Object itemId) {
        //return true;
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).canHasChildren(itemId);
    }

    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        //return true;
        return false; // due to vaadin javadoc, return false if method is not implemented
    }

    public boolean isRoot(Object itemId) {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).isRoot(itemId);
    }

    public boolean hasChildren(Object itemId) {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).hasChildren(itemId);
    }
}
