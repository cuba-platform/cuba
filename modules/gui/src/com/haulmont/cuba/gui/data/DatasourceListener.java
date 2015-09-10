/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;

/**
 * Listener to basic datasource events.
 *
 * @deprecated Use new methods
 *
 * @param <T> type of entity the datasource contains
 *
 * @author abramov
 * @version $Id$
 */
@Deprecated
public interface DatasourceListener<T extends Entity> extends ValueListener<T> {

    /**
     * Current item changed, that is now {@link com.haulmont.cuba.gui.data.Datasource#getItem()} returns a different
     * instance.
     *
     * @param ds       datasource
     * @param prevItem previous selected item
     * @param item     current item
     */
    void itemChanged(Datasource<T> ds, @Nullable T prevItem, @Nullable T item);

    /**
     * Datasource state changed.
     *
     * @param ds        datasource
     * @param prevState previous state
     * @param state     current state
     */
    void stateChanged(Datasource<T> ds, Datasource.State prevState, Datasource.State state);
}