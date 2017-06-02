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

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.events.EventRouter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

public class CollectionDsListenersWrapper implements
        Datasource.ItemChangeListener,
        Datasource.ItemPropertyChangeListener,
        Datasource.StateChangeListener,
        CollectionDatasource.CollectionChangeListener {

    protected WeakItemChangeListener weakItemChangeListener;
    protected WeakItemPropertyChangeListener weakItemPropertyChangeListener;
    protected WeakStateChangeListener weakStateChangeListener;
    protected WeakCollectionChangeListener weakCollectionChangeListener;

    private EventRouter eventRouter;

    /**
     * Use EventRouter for listeners instead of fields with listeners List.
     *
     * @return lazily initialized {@link EventRouter} instance.
     * @see EventRouter
     */
    protected EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
        if (getEventRouter().hasListeners(CollectionDatasource.CollectionChangeListener.class)) {
            getEventRouter().fireEvent(CollectionDatasource.CollectionChangeListener.class,
                    CollectionDatasource.CollectionChangeListener::collectionChanged, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemChanged(Datasource.ItemChangeEvent e) {
        if (getEventRouter().hasListeners(Datasource.ItemChangeListener.class)) {
            getEventRouter().fireEvent(Datasource.ItemChangeListener.class,
                    Datasource.ItemChangeListener::itemChanged, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        if (getEventRouter().hasListeners(Datasource.ItemPropertyChangeListener.class)) {
            getEventRouter().fireEvent(Datasource.ItemPropertyChangeListener.class,
                    Datasource.ItemPropertyChangeListener::itemPropertyChanged, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void stateChanged(Datasource.StateChangeEvent e) {
        if (getEventRouter().hasListeners(Datasource.StateChangeListener.class)) {
            getEventRouter().fireEvent(Datasource.StateChangeListener.class,
                    Datasource.StateChangeListener::stateChanged, e);
        }
    }

    public void addCollectionChangeListener(CollectionDatasource.CollectionChangeListener listener) {
        getEventRouter().addListener(CollectionDatasource.CollectionChangeListener.class, listener);
    }

    public void removeCollectionChangeListener(CollectionDatasource.CollectionChangeListener listener) {
        getEventRouter().removeListener(CollectionDatasource.CollectionChangeListener.class, listener);
    }

    public void addItemChangeListener(Datasource.ItemChangeListener listener) {
        getEventRouter().addListener(Datasource.ItemChangeListener.class, listener);
    }

    public void removeItemChangeListener(Datasource.ItemChangeListener listener) {
        getEventRouter().removeListener(Datasource.ItemChangeListener.class, listener);
    }

    public void addItemPropertyChangeListener(Datasource.ItemPropertyChangeListener listener) {
        getEventRouter().addListener(Datasource.ItemPropertyChangeListener.class, listener);
    }

    public void removeItemPropertyChangeListener(Datasource.ItemPropertyChangeListener listener) {
        getEventRouter().removeListener(Datasource.ItemPropertyChangeListener.class, listener);
    }

    public void addStateChangeListener(Datasource.StateChangeListener listener) {
        getEventRouter().addListener(Datasource.StateChangeListener.class, listener);
    }

    public void removeStateChangeListener(Datasource.StateChangeListener listener) {
        getEventRouter().removeListener(Datasource.StateChangeListener.class, listener);
    }

    @SuppressWarnings("unchecked")
    public void bind(CollectionDatasource ds) {
        weakItemChangeListener = new WeakItemChangeListener(ds, this);
        ds.addItemChangeListener(weakItemChangeListener);

        weakItemPropertyChangeListener = new WeakItemPropertyChangeListener(ds, this);
        ds.addItemPropertyChangeListener(weakItemPropertyChangeListener);

        weakStateChangeListener = new WeakStateChangeListener(ds, this);
        ds.addStateChangeListener(weakStateChangeListener);

        weakCollectionChangeListener = new WeakCollectionChangeListener(ds, this);
        ds.addCollectionChangeListener(weakCollectionChangeListener);
    }

    @SuppressWarnings("unchecked")
    public void unbind(CollectionDatasource ds) {
        ds.removeItemChangeListener(weakItemChangeListener);
        weakItemChangeListener = null;

        ds.removeItemPropertyChangeListener(weakItemPropertyChangeListener);
        weakItemPropertyChangeListener = null;

        ds.removeStateChangeListener(weakStateChangeListener);
        weakStateChangeListener = null;

        ds.removeCollectionChangeListener(weakCollectionChangeListener);
        weakCollectionChangeListener = null;
    }
}
