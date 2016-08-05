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
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

import java.lang.ref.WeakReference;

public class WeakDsListenerAdapter implements Datasource.ItemPropertyChangeListener, Datasource.StateChangeListener, CollectionDatasource.CollectionChangeListener {

    private final CollectionDatasource collectionDatasource;
    private final WeakReference<Datasource.ItemPropertyChangeListener> itemPropertyChangeListenerReference;
    private final WeakReference<Datasource.StateChangeListener> stateChangeListenerReference;
    private final WeakReference<CollectionDatasource.CollectionChangeListener> collectionChangeListenerReference;

    public WeakDsListenerAdapter(CollectionDatasource datasource, Datasource.ItemPropertyChangeListener itemPropertyChangeListener,
                                 Datasource.StateChangeListener stateChangeListener, CollectionDatasource.CollectionChangeListener collectionChangeListener) {
        this.collectionDatasource = datasource;

        this.itemPropertyChangeListenerReference = new WeakReference<>(itemPropertyChangeListener);
        this.stateChangeListenerReference = new WeakReference<>(stateChangeListener);
        this.collectionChangeListenerReference = new WeakReference<>(collectionChangeListener);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
        CollectionDatasource.CollectionChangeListener collectionChangeListener = collectionChangeListenerReference.get();
        if (collectionChangeListener != null) {
            collectionChangeListener.collectionChanged(e);
        } else {
            collectionDatasource.removeCollectionChangeListener(this);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        Datasource.ItemPropertyChangeListener itemPropertyChangeListener = itemPropertyChangeListenerReference.get();
        if (itemPropertyChangeListener != null) {
            itemPropertyChangeListener.itemPropertyChanged(e);
        } else {
            collectionDatasource.removeItemPropertyChangeListener(this);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void stateChanged(Datasource.StateChangeEvent e) {
        Datasource.StateChangeListener stateChangeListener = stateChangeListenerReference.get();
        if (stateChangeListener != null) {
            stateChangeListener.stateChanged(e);
        } else {
            collectionDatasource.removeStateChangeListener(this);
        }
    }
}
