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

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.Datasource.ItemChangeListener;

import java.lang.ref.WeakReference;

/**
 */
public class WeakItemChangeListener implements ItemChangeListener {

    private final Datasource datasource;
    private final WeakReference<ItemChangeListener> reference;

    public WeakItemChangeListener(Datasource datasource, ItemChangeListener itemChangeListener) {
        this.datasource = datasource;
        this.reference = new WeakReference<>(itemChangeListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemChanged(Datasource.ItemChangeEvent e) {
        ItemChangeListener itemChangeListener = reference.get();
        if (itemChangeListener != null) {
            itemChangeListener.itemChanged(e);
        } else {
            datasource.removeItemChangeListener(this);
        }
    }
}