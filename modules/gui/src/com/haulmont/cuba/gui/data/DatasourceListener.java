/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 18:10:33
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

public interface DatasourceListener<T extends Entity> extends ValueListener<T> {
    void itemChanged(Datasource<T> ds, T prevItem, T item);
    void stateChanged(Datasource<T> ds, Datasource.State prevState, Datasource.State state);
}
