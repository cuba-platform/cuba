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

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource.CollectionChangeListener;

import java.lang.ref.WeakReference;

public class WeakCollectionChangeListener implements CollectionChangeListener {

    private final WeakReference<CollectionChangeListener> reference;
    private final CollectionDatasource collectionDatasource;

    public WeakCollectionChangeListener(CollectionDatasource collectionDatasource,
                                        CollectionChangeListener collectionChangeListener) {
        this.collectionDatasource = collectionDatasource;
        this.reference = new WeakReference<>(collectionChangeListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
        CollectionChangeListener collectionChangeListener = reference.get();
        if (collectionChangeListener != null) {
            collectionChangeListener.collectionChanged(e);
        } else {
            collectionDatasource.removeCollectionChangeListener(this);
        }
    }
}