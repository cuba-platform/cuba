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

package com.haulmont.cuba.gui.components.data;

import com.haulmont.bali.events.EventPublisher;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.model.InstanceContainer;

import java.util.Objects;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class ContainerValueSource<E extends Entity, V> extends EventPublisher implements EntityValueSource<E, V> {

    private final InstanceContainer<E> container;
    private final MetaPropertyPath metaPropertyPath;

    protected BindingState state = BindingState.INACTIVE;

    public ContainerValueSource(InstanceContainer<E> container, String property) {
        checkNotNullArgument(container);
        checkNotNullArgument(property);

        MetaClass metaClass = container.getMetaClass();

        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        MetaPropertyPath metaPropertyPath = metadataTools.resolveMetaPropertyPath(metaClass, property);

        checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        this.metaPropertyPath = metaPropertyPath;
        this.container = container;

        this.container.addItemChangeListener(this::containerItemChanged);
        this.container.addItemPropertyChangeListener(this::containerItemPropertyChanged);
    }

    @Override
    public MetaClass getMetaClass() {
        return container.getMetaClass();
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public E getItem() {
        return container.getItem();
    }

    @Override
    public V getValue() {
        E item = container.getItem();
        if (item != null) {
            // todo implement getValueEx with metaPropertyPath
            return item.getValueEx(metaPropertyPath.toPathString());
        }
        return null;
    }

    @Override
    public void setValue(V value) {
        E item = container.getItem();
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
        return container.getItem() == null ? BindingState.INACTIVE : BindingState.ACTIVE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addInstanceChangeListener(Consumer<InstanceChangeEvent<E>> listener) {
        return subscribe(InstanceChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent<V>> listener) {
        return subscribe(StateChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<V>> listener) {
        return subscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    public void setState(BindingState state) {
        if (this.state != state) {
            this.state = state;

            publish(StateChangeEvent.class, new StateChangeEvent<>(this,  BindingState.ACTIVE));
        }
    }

    @SuppressWarnings("unchecked")
    protected void containerItemChanged(InstanceContainer.ItemChangeEvent e) {
        if (e.getItem() != null) {
            setState(BindingState.ACTIVE);
        } else {
            setState(BindingState.INACTIVE);
        }

        publish(InstanceChangeEvent.class, new InstanceChangeEvent(this, e.getPrevItem(), e.getItem()));
    }

    @SuppressWarnings("unchecked")
    protected void containerItemPropertyChanged(InstanceContainer.ItemPropertyChangeEvent e) {
        if (Objects.equals(e.getProperty(), metaPropertyPath.toPathString())) {
            publish(ValueChangeEvent.class, new ValueChangeEvent<>(this, (V)e.getPrevValue(), (V)e.getValue()));
        }
    }
}