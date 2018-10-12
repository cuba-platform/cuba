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

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * The root interface in the <i>data containers</i> hierarchy. Data containers represent a thin layer between
 * visual components and entity instances and collections.
 * <p>
 * {@code InstanceContainer} holds a single entity instance.
 *
 * @see CollectionContainer
 */
public interface InstanceContainer<E extends Entity> {

    /**
     * Returns the contained entity instance.
     * @throws IllegalStateException if there is no entity in the container
     */
    E getItem();

    /**
     * Returns the contained entity instance or null if there is no entity in the container.
     */
    @Nullable
    E getItemOrNull();

    /**
     * Sets the given entity instance to the container.
     */
    void setItem(@Nullable E entity);

    /**
     * Returns the meta-class of entities that can be stored in the container.
     */
    MetaClass getEntityMetaClass();

    /**
     * Returns the view which was set by previous call to {@link #setView(View)}.
     * The view is normally used when loading entities for this container.
     */
    @Nullable
    View getView();

    /**
     * Sets a view to be used when loading entities for this container.
     */
    void setView(View view);

    /**
     * Event sent on changing a property value of the contained entity instance.
     */
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

        /**
         * Returns the container which sent the event.
         */
        @SuppressWarnings("unchecked")
        @Override
        public InstanceContainer<T> getSource() {
            return (InstanceContainer) super.getSource();
        }

        /**
         * Returns an entity instance which property value was changed.
         */
        public T getItem() {
            return item;
        }

        /**
         * Returns the property name.
         */
        public String getProperty() {
            return property;
        }

        /**
         * Returns the previous value of the entity property.
         */
        @Nullable
        public Object getPrevValue() {
            return prevValue;
        }

        /**
         * Returns the current value of the entity property.
         */
        @Nullable
        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "ItemPropertyChangeEvent{" +
                    "item=" + item +
                    ", property='" + property + '\'' +
                    ", prevValue=" + prevValue +
                    ", value=" + value +
                    ", source=" + source +
                    '}';
        }
    }

    /**
     * Adds listener to {@link ItemPropertyChangeEvent}s.
     */
    Subscription addItemPropertyChangeListener(Consumer<ItemPropertyChangeEvent<E>> listener);

    /**
     * Event sent when the entity instance selected in the container is replaced with another instance or null.
     */
    class ItemChangeEvent<T extends Entity> extends EventObject {

        private final T prevItem;
        private final T item;

        public ItemChangeEvent(InstanceContainer<T> container, T prevItem, T item) {
            super(container);
            this.prevItem = prevItem;
            this.item = item;
        }

        /**
         * Returns the container which sent the event.
         */
        @SuppressWarnings("unchecked")
        @Override
        public InstanceContainer<T> getSource() {
            return (InstanceContainer) super.getSource();
        }

        /**
         * Returns the currently selected entity instance.
         */
        @Nullable
        public T getItem() {
            return item;
        }

        /**
         * Returns the previously selected entity instance.
         */
        @Nullable
        public T getPrevItem() {
            return prevItem;
        }

        @Override
        public String toString() {
            return "ItemChangeEvent{" +
                    "prevItem=" + prevItem +
                    ", item=" + item +
                    ", source=" + source +
                    '}';
        }
    }

    /**
     * Adds listener to {@link ItemChangeEvent}s.
     */
    Subscription addItemChangeListener(Consumer<ItemChangeEvent<E>> listener);

}
