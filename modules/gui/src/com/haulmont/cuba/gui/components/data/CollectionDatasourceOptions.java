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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class CollectionDatasourceOptions<E extends Entity<K>, K> implements OptionsSource<E> {

    protected CollectionDatasource<E, K> datasource;
    protected EventPublisher events = new EventPublisher();

    public CollectionDatasourceOptions(CollectionDatasource<E, K> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Stream<E> getOptions() {
        // todo
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<E> getType() {
        return datasource.getMetaClass().getJavaClass();
    }

    @Override
    public BindingState getState() {
        // todo
        return null;
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