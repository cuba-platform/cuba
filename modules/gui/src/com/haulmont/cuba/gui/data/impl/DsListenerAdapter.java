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
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;

import javax.annotation.Nullable;

/**
 * Adapter class for {@link DatasourceListener}. Use it if you need to implement only few methods.
 *
 * @deprecated Use new listeners <br/>
 * {@link com.haulmont.cuba.gui.data.Datasource.ItemChangeListener} <br/>
 * {@link com.haulmont.cuba.gui.data.Datasource.ItemPropertyChangeListener} <br/>
 * {@link com.haulmont.cuba.gui.data.Datasource.StateChangeListener} <br/>
 * {@link com.haulmont.cuba.gui.data.CollectionDatasource.CollectionChangeListener}
 *
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