/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.components.data.options;

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.EntityOptionsSource;
import com.haulmont.cuba.gui.components.data.OptionsSource;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CollectionContainerOptions<E extends Entity<K>, K> implements OptionsSource<E>, EntityOptionsSource<E> {

    protected CollectionContainer<E> container;

    private CollectionLoader loader;

    protected EventHub events = new EventHub();

    protected BindingState state = BindingState.INACTIVE;

    protected E deferredSelectedItem;

    public CollectionContainerOptions(CollectionContainer<E> container, @Nullable CollectionLoader loader) {
        this.container = container;
        this.loader = loader;

        this.container.addCollectionChangeListener(this::containerCollectionChanged);
        this.container.addItemPropertyChangeListener(this::containerItemPropertyChanged);
    }

    protected void containerCollectionChanged(CollectionContainer.CollectionChangeEvent<E> e) {
        if (deferredSelectedItem != null) {
            container.setItem(deferredSelectedItem);
            deferredSelectedItem = null;
        }
        events.publish(OptionsChangeEvent.class, new OptionsChangeEvent<>(this));
    }

    @SuppressWarnings("unchecked")
    protected void containerItemPropertyChanged(CollectionContainer.ItemPropertyChangeEvent<E> e) {
        events.publish(ValueChangeEvent.class, new ValueChangeEvent(this, e.getPrevValue(), e.getValue()));
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return container.getEntityMetaClass();
    }

    @Override
    public void setSelectedItem(E item) {
        if (item == null) {
            container.setItem(null);
        } else {
            if (container.getItems().size() > 0) {
                container.setItem(item);
            } else {
                this.deferredSelectedItem = item;
            }
        }
    }

    @Override
    public boolean containsItem(E item) {
        return item != null && container.getItemIndex(item.getId()) > -1;
    }

    @Override
    public void updateItem(E item) {
        container.replaceItem(item);
    }

    @Override
    public void refresh() {
       if (loader != null)
           loader.load();
    }

    @Override
    public void refresh(Map<String, Object> parameters) {
        if (loader != null)
            loader.load();
    }

    @Override
    public Stream<E> getOptions() {
        return container.getItems().stream();
    }

    @Override
    public BindingState getState() {
        return BindingState.ACTIVE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent<E>> listener) {
        return events.subscribe(StateChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<E>> listener) {
        return events.subscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addOptionsChangeListener(Consumer<OptionsChangeEvent<E>> listener) {
        return events.subscribe(OptionsChangeEvent.class, (Consumer) listener);
    }
}
