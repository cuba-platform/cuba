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
import com.haulmont.cuba.gui.data.Datasource.ItemPropertyChangeListener;

import java.lang.ref.WeakReference;

public class WeakItemPropertyChangeListener implements ItemPropertyChangeListener {

    private final Datasource datasource;
    private final WeakReference<ItemPropertyChangeListener> reference;

    public WeakItemPropertyChangeListener(Datasource datasource,
                                          ItemPropertyChangeListener itemPropertyChangeListener) {
        this.datasource = datasource;
        this.reference = new WeakReference<>(itemPropertyChangeListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        ItemPropertyChangeListener itemPropertyChangeListener = reference.get();
        if (itemPropertyChangeListener != null) {
            itemPropertyChangeListener.itemPropertyChanged(e);
        } else {
            datasource.removeItemPropertyChangeListener(this);
        }
    }
}