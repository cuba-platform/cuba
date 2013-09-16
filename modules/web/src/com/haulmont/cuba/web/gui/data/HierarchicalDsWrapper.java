/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.vaadin.data.Container;

import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public class HierarchicalDsWrapper extends CollectionDsWrapper implements Container.Hierarchical {
    private String parentPropertyName;

    public HierarchicalDsWrapper(HierarchicalDatasource datasource) {
        super(datasource, true);
        this.parentPropertyName = datasource.getHierarchyPropertyName();
    }

    public HierarchicalDsWrapper(HierarchicalDatasource datasource, Collection<MetaPropertyPath> properties) {
        super(datasource, properties, true);
        this.parentPropertyName = datasource.getHierarchyPropertyName();
    }

    @Override
    public Collection getChildren(Object itemId) {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).getChildren(itemId);
    }

    @Override
    public Object getParent(Object itemId) {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).getParent(itemId);
    }

    @Override
    public Collection rootItemIds() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).getRootItemIds();
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        Instance item = datasource.getItem(itemId);
        if (item != null) {
            item.setValue(parentPropertyName, datasource.getItem(newParentId));
            return true;
        }
        return false;
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).canHasChildren(itemId);
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        return false; // due to vaadin javadoc, return false if method is not implemented
    }

    @Override
    public boolean isRoot(Object itemId) {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).isRoot(itemId);
    }

    @Override
    public boolean hasChildren(Object itemId) {
        return ((HierarchicalDatasource<Entity<Object>, Object>) datasource).hasChildren(itemId);
    }
}