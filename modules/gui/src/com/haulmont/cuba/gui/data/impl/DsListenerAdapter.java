/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;

import javax.annotation.Nullable;

/**
 * Adapter class for {@link DatasourceListener}. Use it if you need to implement only few methods.
 *
 * @author tulupov
 * @version $Id$
 */
@Deprecated
public class DsListenerAdapter<T extends Entity> implements DatasourceListener<T> {

    @Override
    public void itemChanged(Datasource<T> ds, @Nullable T prevItem, @Nullable T item) {
    }

    @Override
    public void stateChanged(Datasource<T> ds, Datasource.State prevState, Datasource.State state) {
    }

    @Override
    public void valueChanged(T source, String property, @Nullable Object prevValue, @Nullable Object value) {
    }
}