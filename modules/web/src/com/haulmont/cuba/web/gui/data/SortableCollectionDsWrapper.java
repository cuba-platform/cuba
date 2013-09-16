/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class SortableCollectionDsWrapper extends CollectionDsWrapper implements Container.Sortable {
    public SortableCollectionDsWrapper(CollectionDatasource datasource) {
        super(datasource);
    }

    public SortableCollectionDsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
        super(datasource, autoRefresh);
    }

    public SortableCollectionDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties) {
        super(datasource, properties);
    }

    public SortableCollectionDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties, boolean autoRefresh) {
        super(datasource, properties, autoRefresh);
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        List<CollectionDatasource.Sortable.SortInfo> infos = new ArrayList<>();
        for (int i = 0; i < propertyId.length; i++) {
            final MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId[i];

            final CollectionDatasource.Sortable.SortInfo<MetaPropertyPath> info =
                    new CollectionDatasource.Sortable.SortInfo<>();
            info.setPropertyPath(propertyPath);
            info.setOrder(ascending[i] ? CollectionDatasource.Sortable.Order.ASC : CollectionDatasource.Sortable.Order.DESC);

            infos.add(info);
        }
        ((CollectionDatasource.Sortable) datasource).sort(infos.toArray(new CollectionDatasource.Sortable.SortInfo[infos.size()]));
    }

    @Override
    public Collection getSortableContainerPropertyIds() {
        return properties;
    }

    @Override
    public Object nextItemId(Object itemId) {
        return ((CollectionDatasource.Sortable) datasource).nextItemId(itemId);
    }

    @Override
    public Object prevItemId(Object itemId) {
        return ((CollectionDatasource.Sortable) datasource).prevItemId(itemId);
    }

    @Override
    public Object firstItemId() {
        return ((CollectionDatasource.Sortable) datasource).firstItemId();
    }

    @Override
    public Object lastItemId() {
        return ((CollectionDatasource.Sortable) datasource).lastItemId();
    }

    @Override
    public boolean isFirstId(Object itemId) {
        return ((CollectionDatasource.Sortable) datasource).isFirstId(itemId);
    }

    @Override
    public boolean isLastId(Object itemId) {
        return ((CollectionDatasource.Sortable) datasource).isLastId(itemId);
    }

    @Override
    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}