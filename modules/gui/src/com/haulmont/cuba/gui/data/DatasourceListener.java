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

import javax.annotation.Nullable;

/**
 * Listener to basic datasource events.
 *
 * @deprecated Use new methods
 *
 * @param <T> type of entity the datasource contains
 *
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