/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 18:10:33
 * $Id$
 */
package com.haulmont.cuba.gui.data;

public interface DatasourceListener<T> extends ValueListener<T> {
    void currentChanged(Datasource<T> ds, T prevItem, T item);
    void stateChanged(Datasource<T> ds, Datasource.State prevState, Datasource.State state);
}
