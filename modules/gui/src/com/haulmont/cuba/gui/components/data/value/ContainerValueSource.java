/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components.data.value;

import com.google.common.base.Joiner;
import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.model.DataContextFactory;
import com.haulmont.cuba.gui.model.InstanceContainer;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class ContainerValueSource<E extends Entity, V> implements EntityValueSource<E, V> {

    private final InstanceContainer<E> container;
    private final MetaPropertyPath metaPropertyPath;

    protected BindingState state = BindingState.INACTIVE;

    protected EventHub events = new EventHub();

    @SuppressWarnings("unchecked")
    public ContainerValueSource(InstanceContainer<E> container, String property) {
        checkNotNullArgument(container);
        checkNotNullArgument(property);

        MetaClass metaClass = container.getEntityMetaClass();

        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        MetaPropertyPath metaPropertyPath = metadataTools.resolveMetaPropertyPath(metaClass, property);
        if (metaPropertyPath == null)
            throw new IllegalArgumentException(String.format(
                    "Could not resolve property path '%s' in '%s'", property, metaClass));

        this.metaPropertyPath = metaPropertyPath;
        this.container = container;

        this.container.addItemChangeListener(this::containerItemChanged);
        this.container.addItemPropertyChangeListener(this::containerItemPropertyChanged);

        InstanceContainer parentCont = container;
        for (int i = 1; i < this.metaPropertyPath.length(); i++) {
            MetaPropertyPath intermediatePath = new MetaPropertyPath(this.metaPropertyPath.getMetaClass(),
                    Arrays.copyOf(this.metaPropertyPath.getMetaProperties(), i));
            String pathToTarget = Joiner.on('.').join(
                    Arrays.copyOfRange(this.metaPropertyPath.getPath(), i, this.metaPropertyPath.length()));

            InstanceContainer propertyCont = AppBeans.get(DataContextFactory.class).createInstanceContainer(
                    intermediatePath.getRangeJavaClass());

            parentCont.addItemChangeListener(e -> {
                InstanceContainer.ItemChangeEvent event = (InstanceContainer.ItemChangeEvent) e;
                if (event.getItem() != null) {
                    setState(BindingState.ACTIVE);
                } else {
                    setState(BindingState.INACTIVE);
                }
                propertyCont.setItem(event.getItem() != null ?
                        event.getItem().getValueEx(intermediatePath.getMetaProperty().getName()) : null);
            });

            parentCont.addItemPropertyChangeListener(e -> {
                InstanceContainer.ItemPropertyChangeEvent event = (InstanceContainer.ItemPropertyChangeEvent) e;
                if (Objects.equals(event.getProperty(), intermediatePath.getMetaProperty().getName())) {
                    Entity entity = (Entity) event.getValue();
                    Entity prevEntity = (Entity) event.getPrevValue();
                    propertyCont.setItem(entity);

                    V prevValue = prevEntity != null ? prevEntity.getValueEx(pathToTarget) : null;
                    V value = entity != null ? entity.getValueEx(pathToTarget) : null;
                    events.publish(ValueChangeEvent.class,
                            new ValueChangeEvent<>(this, prevValue, value));
                }
            });

            if (i == this.metaPropertyPath.length() - 1) {
                propertyCont.addItemPropertyChangeListener(e -> {
                    InstanceContainer.ItemPropertyChangeEvent event = (InstanceContainer.ItemPropertyChangeEvent) e;
                    if (Objects.equals(event.getProperty(), this.metaPropertyPath.getMetaProperty().getName())) {
                        events.publish(ValueChangeEvent.class,
                                new ValueChangeEvent<>(this, (V) event.getPrevValue(), (V) event.getValue()));
                    }
                });
            }

            parentCont = propertyCont;
        }
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return container.getEntityMetaClass();
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public E getItem() {
        return container.getItemOrNull();
    }

    @Override
    public V getValue() {
        E item = container.getItemOrNull();
        if (item != null) {
            // todo implement getValueEx with metaPropertyPath
            return item.getValueEx(metaPropertyPath.toPathString());
        }
        return null;
    }

    @Override
    public void setValue(V value) {
        E item = container.getItemOrNull();
        if (item != null) {
            // todo implement setValueEx with metaPropertyPath
            item.setValueEx(metaPropertyPath.toPathString(), value);
        }
    }

    @Override
    public boolean isReadOnly() {
        // todo what about security ?
        return metaPropertyPath.getMetaProperty().isReadOnly();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<V> getType() {
        return metaPropertyPath.getRangeJavaClass();
    }

    @Override
    public BindingState getState() {
        return container.getItemOrNull() == null ? BindingState.INACTIVE : BindingState.ACTIVE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addInstanceChangeListener(Consumer<InstanceChangeEvent<E>> listener) {
        return events.subscribe(InstanceChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent<V>> listener) {
        return events.subscribe(StateChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<V>> listener) {
        return events.subscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    public void setState(BindingState state) {
        if (this.state != state) {
            this.state = state;

            events.publish(StateChangeEvent.class, new StateChangeEvent<>(this,  BindingState.ACTIVE));
        }
    }

    @SuppressWarnings("unchecked")
    protected void containerItemChanged(InstanceContainer.ItemChangeEvent e) {
        if (e.getItem() != null) {
            setState(BindingState.ACTIVE);
        } else {
            setState(BindingState.INACTIVE);
        }

        events.publish(InstanceChangeEvent.class, new InstanceChangeEvent(this, e.getPrevItem(), e.getItem()));
    }

    @SuppressWarnings("unchecked")
    protected void containerItemPropertyChanged(InstanceContainer.ItemPropertyChangeEvent e) {
        if (Objects.equals(e.getProperty(), metaPropertyPath.toPathString())) {
            events.publish(ValueChangeEvent.class, new ValueChangeEvent<>(this, (V)e.getPrevValue(), (V)e.getValue()));
        }
    }
}