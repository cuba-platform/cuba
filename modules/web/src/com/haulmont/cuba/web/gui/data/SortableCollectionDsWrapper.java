/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 30.03.2009 12:37:37
 * $Id$
 */

package com.haulmont.cuba.web.gui.data;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.chile.core.model.MetaProperty;
import com.itmill.toolkit.data.Container;
import com.itmill.toolkit.data.Item;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class SortableCollectionDsWrapper extends CollectionDsWrapper implements Container.Sortable {
    public SortableCollectionDsWrapper(CollectionDatasource datasource) {
        super(datasource);
    }

    public SortableCollectionDsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
        super(datasource, autoRefresh);
    }

    public void sort(Object[] propertyId, boolean[] ascending) {
        List<CollectionDatasource.Sortable.SortInfo> infos = new ArrayList<CollectionDatasource.Sortable.SortInfo>();
        for (int i = 0; i < propertyId.length; i++) {
            final MetaProperty id = (MetaProperty) propertyId[i];
            final CollectionDatasource.Sortable.SortInfo<MetaProperty> info =
                    new CollectionDatasource.Sortable.SortInfo<MetaProperty>();
            info.setProperty(id);
            info.setOrder(ascending[i] ? CollectionDatasource.Sortable.Order.ASC : CollectionDatasource.Sortable.Order.DESC);

            infos.add(info);
        }
        ((CollectionDatasource.Sortable<Entity, Object>) datasource).sort(infos.toArray(new CollectionDatasource.Sortable.SortInfo[infos.size()]));
    }

    public Collection getSortableContainerPropertyIds() {
        return properties;
    }

    public Object nextItemId(Object itemId) {
        return ((CollectionDatasource.Sortable<Entity, Object>) datasource).nextItemId(itemId);
    }

    public Object prevItemId(Object itemId) {
        return ((CollectionDatasource.Sortable<Entity, Object>) datasource).prevItemId(itemId);
    }

    public Object firstItemId() {
        return ((CollectionDatasource.Sortable<Entity, Object>) datasource).firstItemId();
    }

    public Object lastItemId() {
        return ((CollectionDatasource.Sortable<Entity, Object>) datasource).lastItemId();
    }

    public boolean isFirstId(Object itemId) {
        return ((CollectionDatasource.Sortable<Entity, Object>) datasource).isFirstId(itemId);
    }

    public boolean isLastId(Object itemId) {
        return ((CollectionDatasource.Sortable<Entity, Object>) datasource).isLastId(itemId);
    }

    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new  UnsupportedOperationException();
    }

    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
