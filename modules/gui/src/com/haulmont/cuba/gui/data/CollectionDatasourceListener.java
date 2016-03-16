/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import java.util.List;

/**
 * Listener to {@link CollectionDatasource} events.
 *
 * @deprecated Use {@link com.haulmont.cuba.gui.data.CollectionDatasource.CollectionChangeListener}
 * @see com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter
 *
 * @param <T> type of entity the datasource contains
 *
 */
@Deprecated
public interface CollectionDatasourceListener<T extends Entity> extends DatasourceListener<T> {

    /**
     * Operation which caused the datasource change.
     */
    enum Operation {
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