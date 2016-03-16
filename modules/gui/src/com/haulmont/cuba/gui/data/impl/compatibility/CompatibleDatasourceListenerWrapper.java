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

package com.haulmont.cuba.gui.data.impl.compatibility;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;

/**
 */
@SuppressWarnings("unchecked")
@Deprecated
public class CompatibleDatasourceListenerWrapper implements Datasource.ItemChangeListener,
                                                            Datasource.StateChangeListener,
                                                            Datasource.ItemPropertyChangeListener,
                                                            CollectionDatasource.CollectionChangeListener {
    private final DatasourceListener listener;

    public CompatibleDatasourceListenerWrapper(DatasourceListener listener) {
        this.listener = listener;
    }

    @Override
    public void itemChanged(Datasource.ItemChangeEvent e) {
        listener.itemChanged(e.getDs(), e.getPrevItem(), e.getItem());
    }

    @Override
    public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        listener.valueChanged(e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue());
    }

    @Override
    public void stateChanged(Datasource.StateChangeEvent e) {
        listener.stateChanged(e.getDs(), e.getPrevState(), e.getState());
    }

    @Override
    public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
        if (listener instanceof CollectionDatasourceListener) {
            CollectionDatasourceListener.Operation operation;

            switch (e.getOperation()) {
                case ADD:
                    operation = CollectionDatasourceListener.Operation.ADD;
                    break;

                case CLEAR:
                    operation = CollectionDatasourceListener.Operation.CLEAR;
                    break;

                case REFRESH:
                    operation = CollectionDatasourceListener.Operation.REFRESH;
                    break;

                case REMOVE:
                    operation = CollectionDatasourceListener.Operation.REMOVE;
                    break;

                case UPDATE:
                    operation = CollectionDatasourceListener.Operation.UPDATE;
                    break;

                default:
                    throw new IllegalStateException(
                            "Unable to convert CollectionDatasourceListener.Operation to legacy CollectionDatasourceListener.Operation");
            }

            ((CollectionDatasourceListener) listener).collectionChanged(e.getDs(), operation, e.getItems());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CompatibleDatasourceListenerWrapper that = (CompatibleDatasourceListenerWrapper) obj;

        return this.listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }
}