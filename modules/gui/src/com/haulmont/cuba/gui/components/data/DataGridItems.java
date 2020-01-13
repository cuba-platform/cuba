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
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A common interface for providing data for {@link com.haulmont.cuba.gui.components.DataGrid} component.
 *
 * @param <T> row item type
 */
public interface DataGridItems<T> extends DataUnit {

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
     * @param item the item for obtaining the index
     * @return the index of the given item
     */
    int indexOfItem(T item);

    /**
     * @param index the item index
     * @return the item by the given index
     */
    @Nullable
    T getItemByIndex(int index);

    /**
     * @return the stream of all items
     */
    Stream<T> getItems();

    /**
     * @param startIndex    the start index
     * @param numberOfItems the number of items
     * @return items from the {@code startIndex} and size not exceeding the specified number
     */
    List<T> getItems(int startIndex, int numberOfItems);

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
     * @return the current item contained in the source
     */
    @Nullable
    T getSelectedItem();

    /**
     * Set current item in the source.
     *
     * @param item the item to set
     */
    void setSelectedItem(@Nullable T item);

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
     * The DataGridItems that supports sorting.
     *
     * @param <T> items type
     */
    interface Sortable<T> extends DataGridItems<T> {

        void sort(Object[] propertyId, boolean[] ascending);

        void resetSortOrder();

        default void suppressSorting() {
        }

        default void enableSorting() {
        }
    }

    /**
     * An event that is fired when value of item property is changed.
     *
     * @param <T> row item type
     */
    class ValueChangeEvent<T> extends EventObject {
        private final T item;
        private final String property;
        private final Object prevValue;
        private final Object value;

        public ValueChangeEvent(DataGridItems<T> source, T item, String property, Object prevValue, Object value) {
            super(source);
            this.item = item;
            this.property = property;
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataGridItems<T> getSource() {
            return (DataGridItems<T>) super.getSource();
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
     * An event that is fired when item set is changed.
     *
     * @param <T> row item type
     */
    class ItemSetChangeEvent<T> extends EventObject {
        public ItemSetChangeEvent(DataGridItems<T> source) {
            super(source);
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataGridItems<T> getSource() {
            return (DataGridItems<T>) super.getSource();
        }
    }

    /**
     * An event that is fired when selected item is changed.
     *
     * @param <T> row item type
     */
    class SelectedItemChangeEvent<T> extends EventObject {
        protected final T selectedItem;

        public SelectedItemChangeEvent(DataGridItems<T> source, T selectedItem) {
            super(source);
            this.selectedItem = selectedItem;
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataGridItems<T> getSource() {
            return (DataGridItems<T>) super.getSource();
        }

        /**
         * @return a new selected item
         */
        public T getSelectedItem() {
            return selectedItem;
        }
    }
}