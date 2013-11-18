/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import java.util.List;

/**
 * Listener to {@link CollectionDatasource} events.
 *
 * @see com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter
 *
 * @param <T> type of entity the datasource contains
 *
 * @author abramov
 * @version $Id$
 */
public interface CollectionDatasourceListener<T extends Entity> extends DatasourceListener<T> {

    /**
     * Operation which caused the datasource change.
     */
    public enum Operation {
        REFRESH,
        CLEAR,
        ADD,
        REMOVE,
        UPDATE
    }

    /**
     * Enclosed collection changed.
     *
     * @param ds        datasource
     * @param operation operation which caused the datasource change
     * @param items     items which used in operation, in case of {@link Operation#REFRESH} or {@link Operation#CLEAR}
     *                  equals {@link java.util.Collections#emptyList()}
     */
    void collectionChanged(CollectionDatasource ds, Operation operation, List<T> items);
}