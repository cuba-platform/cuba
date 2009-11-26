/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 24.11.2009 17:25:51
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.core.entity.Entity;

public class DsListenerAdapter<T extends Entity> implements DatasourceListener<T> {

    public void itemChanged(Datasource<T> ds, T prevItem, T item) {
    }

    public void stateChanged(Datasource<T> ds, Datasource.State prevState, Datasource.State state) {
    }

    public void valueChanged(T source, String property, Object prevValue, Object value) {
    }
}
