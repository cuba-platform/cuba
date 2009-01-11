/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 16:06:25
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.chile.core.model.Instance;

import java.util.Collection;

public class CollectionPropertyDatasourceImpl<T, K> extends PropertyDatasourceImpl<T> implements CollectionDatasource<T, K> {
    public CollectionPropertyDatasourceImpl(String id, Datasource ds, String property) {
        super(id, ds, property);
    }

    public T getItem(K key) {
        return (T) key;
    }

    public Collection<K> getItemIds() {
        return (Collection<K>) getCollection();
    }

    @Override
    public T getItem() {
        return super.getItem();
    }

    public int size() {
        final Collection<T> collection = getCollection();
        return collection.size();
    }

    protected Collection<T> getCollection() {
        final Instance item = (Instance) ds.getItem();
        return item == null ? null : (Collection<T>) item.getValue(metaProperty.getName());
    }

    public void addItem(T item) throws UnsupportedOperationException {
        getCollection().add(item);
    }

    public void removeItem(T item) throws UnsupportedOperationException {
        getCollection().remove(item);
    }

    public boolean containsItem(K itemId) {
        return getCollection().contains(itemId);
    }

    public String getQuery() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setQuery(String query) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
