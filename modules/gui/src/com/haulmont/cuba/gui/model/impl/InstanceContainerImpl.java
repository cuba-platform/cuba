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

import com.haulmont.bali.events.EventRouter;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.model.InstanceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 *
 */
public class InstanceContainerImpl<T extends Entity> implements InstanceContainer<T> {

    private Logger log = LoggerFactory.getLogger(InstanceContainerImpl.class);

    protected T item;
    protected MetaClass metaClass;

    protected EventRouter eventRouter;
    protected boolean listenersEnabled = true;
    protected Instance.PropertyChangeListener listener = new ItemListener();

    public InstanceContainerImpl(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    @Nullable
    @Override
    public T getItem() {
        return item;
    }

    @Override
    public T getItemNN() {
        T item = getItem();
        if (item == null)
            throw new IllegalStateException("Item is null");
        return item;
    }

    @Override
    public void setItem(T item) {
        T prevItem = this.item;

        if (this.item != null) {
            detachListener(this.item);
        }

        if (item != null) {
            final MetaClass aClass = item.getMetaClass();
            if (!aClass.equals(metaClass) && !metaClass.getDescendants().contains(aClass)) {
                throw new DevelopmentException(String.format("Invalid item's metaClass '%s'", aClass),
                        ParamsMap.of("datasource", toString(), "metaClass", aClass));
            }
            attachListener(item);
        }

        this.item = item;

        fireItemChanged(prevItem);
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Override
    public void addItemPropertyChangeListener(ItemPropertyChangeListener<T> listener) {
        getEventRouter().addListener(ItemPropertyChangeListener.class, listener);
    }

    @Override
    public void removeItemPropertyChangeListener(ItemPropertyChangeListener<T> listener) {
        getEventRouter().removeListener(ItemPropertyChangeListener.class, listener);
    }

    @Override
    public void addItemChangeListener(ItemChangeListener<T> listener) {
        getEventRouter().addListener(ItemChangeListener.class, listener);
    }

    @Override
    public void removeItemChangeListener(ItemChangeListener<T> listener) {
        getEventRouter().removeListener(ItemChangeListener.class, listener);
    }

    protected EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

    protected void fireItemChanged(T prevItem) {
        ItemChangeEvent<T> itemChangeEvent = new ItemChangeEvent<>(this, prevItem, getItem());
        //noinspection unchecked
        getEventRouter().fireEvent(ItemChangeListener.class, ItemChangeListener::itemChanged, itemChangeEvent);
    }

    protected void attachListener(Instance entity) {
        if (entity != null) {
            entity.addPropertyChangeListener(listener);
        }
    }

    protected void detachListener(Instance entity) {
        if (entity != null) {
            entity.removePropertyChangeListener(listener);
        }
    }

    protected class ItemListener implements Instance.PropertyChangeListener {
        @SuppressWarnings("unchecked")
        @Override
        public void propertyChanged(Instance.PropertyChangeEvent e) {
            if (!listenersEnabled) {
                return;
            }

            log.trace("propertyChanged: item={}, property={}, value={}, prevValue={}",
                    e.getItem(), e.getProperty(), e.getValue(), e.getPrevValue());

            ItemPropertyChangeEvent<T> itemPropertyChangeEvent = new ItemPropertyChangeEvent<>(InstanceContainerImpl.this,
                    (T) e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue());

            getEventRouter().fireEvent(ItemPropertyChangeListener.class, ItemPropertyChangeListener::itemPropertyChanged,
                    itemPropertyChangeEvent);
        }
    }

}
