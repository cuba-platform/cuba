/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;

import java.lang.ref.WeakReference;

public class WeakContainerListenerAdapter implements InstanceContainer.ItemPropertyChangeListener, /*EntityContainer.StateChangeListener,*/ CollectionContainer.CollectionChangeListener {

    private final CollectionContainer collectionDatasource;
    private final WeakReference<InstanceContainer.ItemPropertyChangeListener> itemPropertyChangeListenerReference;
//    private final WeakReference<EntityContainer.StateChangeListener> stateChangeListenerReference;
    private final WeakReference<CollectionContainer.CollectionChangeListener> collectionChangeListenerReference;

    public WeakContainerListenerAdapter(CollectionContainer datasource, InstanceContainer.ItemPropertyChangeListener itemPropertyChangeListener,
                                        /*EntityContainer.StateChangeListener stateChangeListener,*/ CollectionContainer.CollectionChangeListener collectionChangeListener) {
        this.collectionDatasource = datasource;

        this.itemPropertyChangeListenerReference = new WeakReference<>(itemPropertyChangeListener);
//        this.stateChangeListenerReference = new WeakReference<>(stateChangeListener);
        this.collectionChangeListenerReference = new WeakReference<>(collectionChangeListener);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void collectionChanged(CollectionContainer.CollectionChangeEvent e) {
        CollectionContainer.CollectionChangeListener collectionChangeListener = collectionChangeListenerReference.get();
        if (collectionChangeListener != null) {
            collectionChangeListener.collectionChanged(e);
        } else {
            collectionDatasource.removeCollectionChangeListener(this);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemPropertyChanged(InstanceContainer.ItemPropertyChangeEvent e) {
        InstanceContainer.ItemPropertyChangeListener itemPropertyChangeListener = itemPropertyChangeListenerReference.get();
        if (itemPropertyChangeListener != null) {
            itemPropertyChangeListener.itemPropertyChanged(e);
        } else {
            collectionDatasource.removeItemPropertyChangeListener(this);
        }
    }

//    @SuppressWarnings("unchecked")
//    @Override
//    public void stateChanged(EntityContainer.StateChangeEvent e) {
//        EntityContainer.StateChangeListener stateChangeListener = stateChangeListenerReference.get();
//        if (stateChangeListener != null) {
//            stateChangeListener.stateChanged(e);
//        } else {
//            collectionDatasource.removeStateChangeListener(this);
//        }
//    }
}
