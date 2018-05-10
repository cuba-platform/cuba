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

import com.haulmont.bali.events.Subscription;

import java.util.Collection;
import java.util.EventObject;
import java.util.function.Consumer;

public interface TableDataSource<I> {
    Collection<?> getItemIds();

    I getItem(Object itemId);

    Object getItemValue(Object itemId, Object propertyId);
    void setItemValue(Object itemId, Object propertyId, Object newValue);

    int size();

    boolean containsId(Object itemId);

    BindingState getState();

    Class<?> getType(Object propertyId);

    boolean supportsProperty(Object propertyId);

    Subscription addStateChangeListener(Consumer<TableDataSource.StateChangeEvent<I>> listener);
    Subscription addValueChangeListener(Consumer<TableDataSource.ValueChangeEvent<I>> listener);
    Subscription addItemSetChangeListener(Consumer<TableDataSource.ItemSetChangeEvent<I>> listener);

    interface Ordered<T> extends TableDataSource<T> {
        Object nextItemId(Object itemId);

        Object prevItemId(Object itemId);

        Object firstItemId();

        Object lastItemId();

        boolean isFirstId(Object itemId);

        boolean isLastId(Object itemId);
    }

    interface Sortable<T> extends Ordered<T> {
        void sort(Object[] propertyId, boolean[] ascending);
    }

    // todo
    class StateChangeEvent<T> extends EventObject {
        protected BindingState state;

        public StateChangeEvent(TableDataSource<T> source, BindingState state) {
            super(source);
            this.state = state;
        }

        @SuppressWarnings("unchecked")
        @Override
        public TableDataSource<T> getSource() {
            return (TableDataSource<T>) super.getSource();
        }

        public BindingState getState() {
            return state;
        }
    }

    // todo
    class ValueChangeEvent<T> extends EventObject {
        private final T prevValue;
        private final T value;

        public ValueChangeEvent(TableDataSource<T> source, T prevValue, T value) {
            super(source);
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public TableDataSource<T> getSource() {
            return (TableDataSource<T>) super.getSource();
        }

        public T getPrevValue() {
            return prevValue;
        }

        public T getValue() {
            return value;
        }
    }

    // todo
    class ItemSetChangeEvent<T> extends EventObject {
        public ItemSetChangeEvent(TableDataSource<T> source) {
            super(source);
        }

        @SuppressWarnings("unchecked")
        @Override
        public TableDataSource<T> getSource() {
            return (TableDataSource<T>) super.getSource();
        }
    }
}