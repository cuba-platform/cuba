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

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.model.InstanceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 *
 */
public class InstanceContainerImpl<T extends Entity> implements InstanceContainer<T> {

    private static final Logger log = LoggerFactory.getLogger(InstanceContainerImpl.class);

    protected T item;
    protected MetaClass entityMetaClass;
    protected View view;

    protected EventHub events = new EventHub();
    protected boolean listenersEnabled = true;
    protected Instance.PropertyChangeListener listener = new ItemListener();

    public InstanceContainerImpl(MetaClass entityMetaClass) {
        this.entityMetaClass = entityMetaClass;
    }

    @Nullable
    @Override
    public T getItemOrNull() {
        return item;
    }

    @Override
    public T getItem() {
        T item = getItemOrNull();
        if (item == null)
            throw new IllegalStateException("Current item is null");
        return item;
    }

    @Override
    public void setItem(@Nullable T item) {
        T prevItem = this.item;

        if (this.item != null) {
            detachListener(this.item);
        }

        if (item != null) {
            final MetaClass aClass = item.getMetaClass();
            if (!aClass.equals(entityMetaClass) && !entityMetaClass.getDescendants().contains(aClass)) {
                throw new DevelopmentException(String.format("Invalid item's metaClass '%s'", aClass),
                        ParamsMap.of("datasource", toString(), "metaClass", aClass));
            }
            attachListener(item);
        }

        this.item = item;

        fireItemChanged(prevItem);
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return entityMetaClass;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addItemPropertyChangeListener(Consumer<ItemPropertyChangeEvent<T>> listener) {
        return events.subscribe(ItemPropertyChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addItemChangeListener(Consumer<ItemChangeEvent<T>> listener) {
        return events.subscribe(ItemChangeEvent.class, (Consumer) listener);
    }

    protected void fireItemChanged(T prevItem) {
        ItemChangeEvent<T> itemChangeEvent = new ItemChangeEvent<>(this, prevItem, getItemOrNull());
        log.trace("itemChanged: {}", itemChangeEvent);
        events.publish(ItemChangeEvent.class, itemChangeEvent);
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

    @Override
    public String toString() {
        return "InstanceContainerImpl{" +
                "entity=" + entityMetaClass +
                ", view=" + view +
                '}';
    }

    protected class ItemListener implements Instance.PropertyChangeListener {
        @SuppressWarnings("unchecked")
        @Override
        public void propertyChanged(Instance.PropertyChangeEvent e) {
            if (!listenersEnabled) {
                return;
            }

            ItemPropertyChangeEvent<T> itemPropertyChangeEvent = new ItemPropertyChangeEvent<>(InstanceContainerImpl.this,
                    (T) e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue());

            log.trace("propertyChanged: {}", itemPropertyChangeEvent);

            events.publish(ItemPropertyChangeEvent.class, itemPropertyChangeEvent);
        }
    }

}
