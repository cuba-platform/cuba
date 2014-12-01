/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class CollectionDsListenerWeakWrapper implements CollectionDatasourceListener, WeakDatasourceListener {

    protected WeakReference<CollectionDatasourceListener> listener;
    protected CollectionDatasource ds;

    public CollectionDsListenerWeakWrapper(CollectionDatasource ds, CollectionDatasourceListener listener) {
        this.ds = ds;
        this.listener = new WeakReference<>(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation, List items) {
        CollectionDatasourceListener listenerImpl = listener.get();
        if (listenerImpl != null) {
            listenerImpl.collectionChanged(ds, operation, items);
        } else {
            removeBridge();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemChanged(Datasource ds, @Nullable Entity prevItem, @Nullable Entity item) {
        CollectionDatasourceListener listenerImpl = listener.get();
        if (listenerImpl != null) {
            listenerImpl.itemChanged(ds, prevItem, item);
        } else {
            removeBridge();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        CollectionDatasourceListener listenerImpl = listener.get();
        if (listenerImpl != null) {
            listenerImpl.stateChanged(ds, prevState, state);
        } else {
            removeBridge();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
        CollectionDatasourceListener listenerImpl = listener.get();
        if (listenerImpl != null) {
            listenerImpl.valueChanged(source, property, prevValue, value);
        } else {
            removeBridge();
        }
    }

    @Override
    public boolean isAlive() {
        return listener.get() != null;
    }

    @SuppressWarnings("unchecked")
    protected void removeBridge() {
        this.ds.removeListener(this);
    }
}