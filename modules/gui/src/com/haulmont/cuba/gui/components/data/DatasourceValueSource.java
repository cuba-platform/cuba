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
import com.haulmont.cuba.gui.data.Datasource;

import java.util.Objects;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

// todo
public class DatasourceValueSource<E extends Entity, V> extends EventPublisher implements EntityValueSource<E, V> {
    protected final Datasource<E> datasource;
    protected final MetaPropertyPath metaPropertyPath;

    protected ValueSourceState state = ValueSourceState.INACTIVE;

    @SuppressWarnings("unchecked")
    public DatasourceValueSource(Datasource<E> datasource, String property) {
        checkNotNullArgument(datasource);
        checkNotNullArgument(property);

        MetaClass metaClass = datasource.getMetaClass();

        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        MetaPropertyPath metaPropertyPath = metadataTools.resolveMetaPropertyPath(metaClass, property);

        checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        this.metaPropertyPath = metaPropertyPath;
        this.datasource = datasource;
        // todo setup listeners:

        this.datasource.addStateChangeListener(e -> {
            if (e.getState() == Datasource.State.VALID) {
                setState(ValueSourceState.ACTIVE);
            } else {
                setState(ValueSourceState.INACTIVE);
            }
        });

        this.datasource.addItemChangeListener(e -> {
            if (e.getItem() != null && datasource.getState() == Datasource.State.VALID) {
                setState(ValueSourceState.ACTIVE);

                publish(InstanceChangeEvent.class, new InstanceChangeEvent<>(this, e.getPrevItem(), e.getItem()));
            }
        });

        this.datasource.addItemPropertyChangeListener(e -> {
            if (Objects.equals(e.getProperty(), property)) {
                publish(ValueChangeEvent.class, new ValueChangeEvent<>(this, (V)e.getPrevValue(), (V)e.getValue()));
            }
        });
    }

    public void setState(ValueSourceState state) {
        if (this.state != state) {
            this.state = state;

            publish(StateChangeEvent.class, new StateChangeEvent<>(this,  ValueSourceState.ACTIVE));
        }
    }

    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaClass getMetaClass() {
        return datasource.getMetaClass();
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public E getItem() {
        return datasource.getItem();
    }

    @Override
    public V getValue() {
        E item = datasource.getItem();
        if (item != null) {
            // todo implement getValueEx with metaPropertyPath
            return item.getValueEx(metaPropertyPath.toPathString());
        }
        return null;
    }

    @Override
    public void setValue(Object value) {
        E item = datasource.getItem();
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
    public ValueSourceState getStatus() {
        if (datasource.getState() == Datasource.State.VALID
                && datasource.getItem() != null) {
            return ValueSourceState.ACTIVE;
        }

        return ValueSourceState.INACTIVE;
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
}