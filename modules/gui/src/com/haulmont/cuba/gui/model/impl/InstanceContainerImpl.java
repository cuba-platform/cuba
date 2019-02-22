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
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.model.HasLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 *
 */
public class InstanceContainerImpl<E extends Entity> implements InstanceContainer<E>, HasLoader, ItemPropertyChangeNotifier {

    private static final Logger log = LoggerFactory.getLogger(InstanceContainerImpl.class);

    protected E item;
    protected MetaClass entityMetaClass;
    protected View view;

    protected EventHub events = new EventHub();
    protected Instance.PropertyChangeListener listener = this::itemPropertyChanged;
    protected DataLoader loader;

    protected boolean listenersEnabled = true;

    public InstanceContainerImpl(MetaClass entityMetaClass) {
        this.entityMetaClass = entityMetaClass;
    }

    @Nullable
    @Override
    public E getItemOrNull() {
        return item;
    }

    @Nonnull
    @Override
    public E getItem() {
        E item = getItemOrNull();
        if (item == null) {
            throw new IllegalStateException("Current item is null");
        }
        return item;
    }

    @Override
    public void setItem(@Nullable E item) {
        E prevItem = this.item;

        if (this.item != null) {
            detachListener(this.item);
        }

        if (item != null) {
            final MetaClass aClass = item.getMetaClass();
            if (!aClass.equals(entityMetaClass) && !entityMetaClass.getDescendants().contains(aClass)) {
                throw new DevelopmentException(String.format("Invalid item's metaClass '%s'", aClass),
                        ParamsMap.of("container", toString(), "metaClass", aClass));
            }
            detachListener(item);
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
    public Subscription addItemPropertyChangeListener(Consumer<ItemPropertyChangeEvent<E>> listener) {
        return events.subscribe(ItemPropertyChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addItemChangeListener(Consumer<ItemChangeEvent<E>> listener) {
        return events.subscribe(ItemChangeEvent.class, (Consumer) listener);
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

    @Nullable
    @Override
    public DataLoader getLoader() {
        return loader;
    }

    @Override
    public void setLoader(DataLoader loader) {
        this.loader = loader;
    }

    protected void fireItemChanged(E prevItem) {
        if (!listenersEnabled) {
            return;
        }

        ItemChangeEvent<E> itemChangeEvent = new ItemChangeEvent<>(this, prevItem, getItemOrNull());
        log.trace("itemChanged: {}", itemChangeEvent);
        events.publish(ItemChangeEvent.class, itemChangeEvent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void itemPropertyChanged(Instance.PropertyChangeEvent e) {
        if (!listenersEnabled) {
            return;
        }

        ItemPropertyChangeEvent<E> itemPropertyChangeEvent = new ItemPropertyChangeEvent<>(InstanceContainerImpl.this,
                (E) e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue());

        log.trace("propertyChanged: {}", itemPropertyChangeEvent);

        events.publish(ItemPropertyChangeEvent.class, itemPropertyChangeEvent);
    }

    @Override
    public void mute() {
        this.listenersEnabled = false;
    }

    @Override
    public void unmute() {
        this.listenersEnabled = true;
    }
}