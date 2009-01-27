/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 15:18:26
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.DsContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PropertyDatasourceImpl<T> implements Datasource<T>, DatasourceImplementation {
    private String id;

    protected Datasource ds;
    protected MetaProperty metaProperty;

    private List<DatasourceListener> dsListeners = new ArrayList<DatasourceListener>();

    public PropertyDatasourceImpl(String id, Datasource ds, String property) {
        this.id = id;
        this.ds = ds;
        metaProperty = ds.getMetaClass().getProperty(property);
    }

    public State getState() {
        return ds.getState();
    }

    public T getItem() {
        final Instance item = (Instance) ds.getItem();
        return item == null ? null : (T) item.getValue(metaProperty.getName());
    }

    public MetaClass getMetaClass() {
        return metaProperty.getRange().asClass();
    }

    public View getView() {
        final ViewProperty property = ds.getView().getProperty(metaProperty.getName());
        return property == null ? null : property.getView();
    }

    public String getId() {
        return id;
    }

    public DsContext getDsContext() {
        return ds.getDsContext();
    }

    public void commit() {
        throw new UnsupportedOperationException();
    }

    public void refresh() {
    }

    public void setItem(T item) {
        throw new UnsupportedOperationException();
    }

    public void invalidate() {
    }

    public Collection<T> getItemsToCreate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<T> getItemsToUpdate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<T> getItemsToDelete() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addListener(DatasourceListener<T> listener) {
        if (dsListeners.indexOf(listener) < 0) {
            dsListeners.add(listener);
        }
    }

    public void removeListener(DatasourceListener<T> listener) {
        dsListeners.remove(listener);
    }

    public void initialized() {
    }
}
