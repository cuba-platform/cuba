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

import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceContainer.ItemChangeListener;

import java.lang.ref.WeakReference;

public class WeakItemChangeListener implements ItemChangeListener {

    private final InstanceContainer container;
    private final WeakReference<ItemChangeListener> reference;

    public WeakItemChangeListener(InstanceContainer datasource, ItemChangeListener itemChangeListener) {
        this.container = datasource;
        this.reference = new WeakReference<>(itemChangeListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemChanged(InstanceContainer.ItemChangeEvent e) {
        ItemChangeListener itemChangeListener = reference.get();
        if (itemChangeListener != null) {
            itemChangeListener.itemChanged(e);
        } else {
            container.removeItemChangeListener(this);
        }
    }
}