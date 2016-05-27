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
import com.haulmont.cuba.gui.data.Datasource.StateChangeListener;

import java.lang.ref.WeakReference;

public class WeakStateChangeListener implements StateChangeListener {

    private final Datasource datasource;
    private final WeakReference<StateChangeListener> reference;

    public WeakStateChangeListener(Datasource datasource, StateChangeListener stateChangeListener) {
        this.datasource = datasource;
        this.reference = new WeakReference<>(stateChangeListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void stateChanged(Datasource.StateChangeEvent e) {
        StateChangeListener stateChangeListener = reference.get();
        if (stateChangeListener != null) {
            stateChangeListener.stateChanged(e);
        } else {
            datasource.removeStateChangeListener(this);
        }
    }
}