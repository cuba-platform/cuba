/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:06:47
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.View;

import java.util.Collection;

public interface Datasource<T> {
    String getId();

    DsContext getDsContext();
    DataService getDataService();

    void commit();

    enum State {
        NOT_INITIALIZAED,
        INVALID,
        VALID
    }

    State getState();

    T getItem();
    void setItem(T item);

    void invalidate();
    void refresh();

    MetaClass getMetaClass();
    View getView();

    Collection<T> getItemsToCreate();
    Collection<T> getItemsToUpdate();
    Collection<T> getItemsToDelete();

    void addListener(DatasourceListener<T> listener);
    void removeListener(DatasourceListener<T> listener);
}
