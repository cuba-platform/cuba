/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

/**
 * @author artamonov
 * @version $Id$
 */
public class DsListenerWeakWrapper implements DatasourceListener {

    protected WeakReference<DatasourceListener> listener;
    protected Datasource ds;

    public DsListenerWeakWrapper(Datasource ds, DatasourceListener listener) {
        this.listener = new WeakReference<>(listener);
        this.ds = ds;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemChanged(Datasource ds, @Nullable Entity prevItem, @Nullable Entity item) {
        DatasourceListener listenerImpl = listener.get();
        if (listenerImpl != null) {
            listenerImpl.itemChanged(ds, prevItem, item);
        } else {
            removeBridge();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        DatasourceListener listenerImpl = listener.get();
        if (listenerImpl != null) {
            listenerImpl.stateChanged(ds, prevState, state);
        } else {
            removeBridge();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
        DatasourceListener listenerImpl = listener.get();
        if (listenerImpl != null) {
            listenerImpl.valueChanged(source, property, prevValue, value);
        } else {
            removeBridge();
        }
    }

    @SuppressWarnings("unchecked")
    protected void removeBridge() {
        this.ds.removeListener(this);
    }
}