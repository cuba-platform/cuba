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

package com.haulmont.cuba.gui.model;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.util.EventObject;

/**
 *
 */
public interface InstanceContainer<T extends Entity> {

    @Nullable
    T getItem();

    T getItemNN();

    void setItem(T entity);

    MetaClass getMetaClass();

    class ItemPropertyChangeEvent<T extends Entity> extends EventObject {
        private final T item;
        private final String property;
        private final Object prevValue;
        private final Object value;

        public ItemPropertyChangeEvent(InstanceContainer<T> container, T item, String property, Object prevValue, Object value) {
            super(container);
            this.item = item;
            this.property = property;
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public InstanceContainer<T> getSource() {
            return (InstanceContainer) super.getSource();
        }

        /**
         * @return item, which property value is changed
         */
        public T getItem() {
            return item;
        }

        /**
         * @return property name
         */
        public String getProperty() {
            return property;
        }

        /**
         * @return previous value of item property
         */
        @Nullable
        public Object getPrevValue() {
            return prevValue;
        }

        /**
         * @return current value of item property
         */
        @Nullable
        public Object getValue() {
            return value;
        }
    }

    /**
     * Listener to datasource item property value change events.
     */
    @FunctionalInterface
    interface ItemPropertyChangeListener<T extends Entity> {
        /**
         * Property value of some datasource item changed. In case of {@link CollectionContainer} this method may be
         * called for any item of collection if its property value changed.
         */
        void itemPropertyChanged(ItemPropertyChangeEvent<T> e);
    }

    // todo provide registration
    void addItemPropertyChangeListener(ItemPropertyChangeListener<T> listener);

    // todo remove
    void removeItemPropertyChangeListener(ItemPropertyChangeListener<T> listener);

    /**
     * Container item change event.
     */
    class ItemChangeEvent<T extends Entity> extends EventObject {

        private final T prevItem;
        private final T item;

        public ItemChangeEvent(InstanceContainer<T> container, T prevItem, T item) {
            super(container);
            this.prevItem = prevItem;
            this.item = item;
        }

        @SuppressWarnings("unchecked")
        @Override
        public InstanceContainer<T> getSource() {
            return (InstanceContainer) super.getSource();
        }

        /**
         * @return current item
         */
        @Nullable
        public T getItem() {
            return item;
        }

        /**
         * @return previously selected item
         */
        @Nullable
        public T getPrevItem() {
            return prevItem;
        }
    }

    /**
     * Listener to container item change events.
     */
    @FunctionalInterface
    interface ItemChangeListener<T extends Entity> {
        /**
         * Current item changed, that is now {@link InstanceContainer#getItem()} returns a different instance.
         */
        void itemChanged(ItemChangeEvent<T> e);
    }

    // todo provide registration
    void addItemChangeListener(ItemChangeListener<T> listener);

    // todo remove
    void removeItemChangeListener(ItemChangeListener<T> listener);

}
