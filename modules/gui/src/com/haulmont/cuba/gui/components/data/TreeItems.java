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

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface TreeItems<T> extends DataUnit {

    /**
     * @param item the item for obtaining the id
     * @return the id on the given item
     */
    Object getItemId(T item);

    /**
     * @param itemId the item id
     * @return the item by the given id
     */
    @Nullable
    T getItem(Object itemId);

    /**
     * @return the stream of all items
     */
    Stream<T> getItems();

    /**
     * @param item an item to check
     * @return {@code true} if the underlying collection contains an item, {@code false} otherwise
     */
    boolean containsItem(T item);

    /**
     * @return size of the underlying collection
     */
    int size();

    /**
     * @param parent the parent item
     * @return child count of the given parent item
     */
    int getChildCount(T parent);

    /**
     * @param item the item to obtain children or {@code null} to get root items
     * @return children of the given item
     */
    Stream<T> getChildren(@Nullable T item);

    /**
     * @param item the item to check
     * @return {@code true} if the item has children, {@code false} otherwise
     */
    boolean hasChildren(T item);

    /**
     * @param item the item to get parent
     * @return the parent of given item or {@code null} if no parent
     */
    @Nullable
    T getParent(T item);

    /**
     * @return the name of the property which forms the hierarchy
     */
    String getHierarchyPropertyName();

    /**
     * Registers a new value change listener.
     *
     * @param listener the listener to be added
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addValueChangeListener(Consumer<ValueChangeEvent<T>> listener);

    /**
     * Registers a new item set change listener.
     *
     * @param listener the listener to be added
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addItemSetChangeListener(Consumer<ItemSetChangeEvent<T>> listener);

    /**
     * Registers a new selected item change listener.
     *
     * @param listener the listener to be added
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addSelectedItemChangeListener(Consumer<SelectedItemChangeEvent<T>> listener);

    /**
     * An event that is fired when TreeItems value is changed.
     *
     * @param <T> the source component type
     */
    class ValueChangeEvent<T> extends EventObject {
        private final T item;
        private final String property;
        private final Object prevValue;
        private final Object value;

        public ValueChangeEvent(TreeItems<T> source, T item, String property, @Nullable Object prevValue, @Nullable Object value) {
            super(source);
            this.item = item;
            this.property = property;
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public TreeItems<T> getSource() {
            return (TreeItems<T>) super.getSource();
        }

        /**
         * @return the item which value is changed
         */
        public T getItem() {
            return item;
        }

        /**
         * @return changed property name
         */
        public String getProperty() {
            return property;
        }

        /**
         * @return a previous value of the item property
         */
        public Object getPrevValue() {
            return prevValue;
        }

        /**
         * @return a new value of the item property
         */
        public Object getValue() {
            return value;
        }
    }

    /**
     * An event that is fired when TreeItems item set is changed.
     *
     * @param <T> the source component type
     */
    class ItemSetChangeEvent<T> extends EventObject {
        public ItemSetChangeEvent(TreeItems<T> source) {
            super(source);
        }

        @SuppressWarnings("unchecked")
        @Override
        public TreeItems<T> getSource() {
            return (TreeItems<T>) super.getSource();
        }
    }

    /**
     * An event that is fired when TreeItems selected item is changed.
     *
     * @param <T> the source component type
     */
    class SelectedItemChangeEvent<T> extends EventObject {
        protected final T selectedItem;

        public SelectedItemChangeEvent(TreeItems<T> source, T selectedItem) {
            super(source);
            this.selectedItem = selectedItem;
        }

        @SuppressWarnings("unchecked")
        @Override
        public TreeItems<T> getSource() {
            return (TreeItems<T>) super.getSource();
        }

        /**
         * @return a new selected item
         */
        public T getSelectedItem() {
            return selectedItem;
        }
    }
}
