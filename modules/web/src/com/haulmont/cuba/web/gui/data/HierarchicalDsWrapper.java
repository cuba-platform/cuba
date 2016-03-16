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
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.vaadin.data.Container;

import java.util.Collection;

/**
 */
public class HierarchicalDsWrapper extends CollectionDsWrapper implements Container.Hierarchical {

    protected String parentPropertyName;

    public HierarchicalDsWrapper(HierarchicalDatasource datasource) {
        super(datasource, true);
        this.parentPropertyName = datasource.getHierarchyPropertyName();
    }

    @Override
    public Collection getChildren(Object itemId) {
        //noinspection unchecked
        return ((HierarchicalDatasource) datasource).getChildren(itemId);
    }

    @Override
    public Object getParent(Object itemId) {
        //noinspection unchecked
        return ((HierarchicalDatasource) datasource).getParent(itemId);
    }

    @Override
    public Collection rootItemIds() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return ((HierarchicalDatasource) datasource).getRootItemIds();
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        //noinspection unchecked
        Instance item = datasource.getItem(itemId);
        if (item != null) {
            //noinspection unchecked
            item.setValue(parentPropertyName, datasource.getItem(newParentId));
            return true;
        }
        return false;
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        //noinspection unchecked
        return ((HierarchicalDatasource) datasource).hasChildren(itemId);
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        return false; // due to vaadin javadoc, return false if method is not implemented
    }

    @Override
    public boolean isRoot(Object itemId) {
        //noinspection unchecked
        return ((HierarchicalDatasource) datasource).isRoot(itemId);
    }

    @Override
    public boolean hasChildren(Object itemId) {
        //noinspection unchecked
        return ((HierarchicalDatasource) datasource).hasChildren(itemId);
    }
}