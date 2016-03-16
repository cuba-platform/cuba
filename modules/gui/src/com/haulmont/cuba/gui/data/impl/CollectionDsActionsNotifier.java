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

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

/**
 */
public class CollectionDsActionsNotifier implements Datasource.ItemChangeListener,
                                                    Datasource.ItemPropertyChangeListener,
                                                    Datasource.StateChangeListener,
                                                    CollectionDatasource.CollectionChangeListener {

    private final Component.ActionsHolder actionsHolder;

    public CollectionDsActionsNotifier(Component.ActionsHolder actionsHolder) {
        this.actionsHolder = actionsHolder;
    }

    @Override
    public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
        for (Action action : actionsHolder.getActions()) {
            action.refreshState();
        }
    }

    @Override
    public void itemChanged(Datasource.ItemChangeEvent e) {
        for (Action action : actionsHolder.getActions()) {
            action.refreshState();
        }
    }

    @Override
    public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        for (Action action : actionsHolder.getActions()) {
            action.refreshState();
        }
    }

    @Override
    public void stateChanged(Datasource.StateChangeEvent e) {
        for (Action action : actionsHolder.getActions()) {
            action.refreshState();
        }
    }

    public void bind(CollectionDatasource ds) {
        ds.addItemChangeListener(this);
        ds.addItemPropertyChangeListener(this);
        ds.addStateChangeListener(this);
        ds.addCollectionChangeListener(this);
    }
}