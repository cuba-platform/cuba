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
import java.util.LinkedList;
import java.util.List;

import static com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.Order;
import static com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.SortInfo;

/**
 * @author abramov
 * @version $Id$
 */
public class SortableCollectionDsWrapper extends CollectionDsWrapper implements Container.Sortable {

    public SortableCollectionDsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
        super(datasource, autoRefresh);
    }

    public SortableCollectionDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties, boolean autoRefresh) {
        super(datasource, properties, autoRefresh);
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        List<SortInfo> infos = new LinkedList<>();
        for (int i = 0; i < propertyId.length; i++) {
            final MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId[i];

            final SortInfo<MetaPropertyPath> info = new SortInfo<>();
            info.setPropertyPath(propertyPath);
            info.setOrder(ascending[i] ? Order.ASC : Order.DESC);

            infos.add(info);
        }
        SortInfo[] sortInfos = infos.toArray(new SortInfo[infos.size()]);
        ((CollectionDatasource.Sortable) datasource).sort(sortInfos);
    }

    @Override
    public Collection getSortableContainerPropertyIds() {
        return properties;
    }

    @Override
    public Object nextItemId(Object itemId) {
        //noinspection unchecked
        return ((CollectionDatasource.Sortable) datasource).nextItemId(itemId);
    }

    @Override
    public Object prevItemId(Object itemId) {
        //noinspection unchecked
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
        //noinspection unchecked
        return ((CollectionDatasource.Sortable) datasource).isFirstId(itemId);
    }

    @Override
    public boolean isLastId(Object itemId) {
        //noinspection unchecked
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