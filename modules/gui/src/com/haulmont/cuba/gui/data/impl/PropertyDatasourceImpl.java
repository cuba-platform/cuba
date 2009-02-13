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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.DataService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PropertyDatasourceImpl<T extends Entity>
    extends
        AbstractDataSource<T>
    implements
        Datasource<T>, DatasourceImplementation<T>
{
    protected Datasource ds;
    protected MetaProperty metaProperty;

    private List<DatasourceListener> dsListeners = new ArrayList<DatasourceListener>();

    public PropertyDatasourceImpl(String id, Datasource ds, String property) {
        super(id);
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

    public DsContext getDsContext() {
        return ds.getDsContext();
    }

    public DataService getDataService() {
        return ds.getDataService();
    }

    public CommitMode getCommitMode() {
        // TODO support embedded
        return CommitMode.DATASTORE;
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


    public void initialized() {
    }

    public void commited(Map<Entity, Entity> map) {
        modified = false;
        clearCommitLists();
    }
}
