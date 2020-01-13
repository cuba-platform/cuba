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
import java.util.Collection;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * A common interface for providing data for {@link com.haulmont.cuba.gui.components.Table} component.
 *
 * @param <I> row item type
 */
public interface TableItems<I> extends DataUnit {
    Collection<?> getItemIds();

    @Nullable
    I getItem(Object itemId);

    default I getItemNN(Object itemId) {
        I item = getItem(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Unable to find item with id " + itemId);
        }
        return item;
    }

    @Nullable
    Object getItemValue(Object itemId, Object propertyId);

    int size();

    boolean containsId(Object itemId);

    Class<?> getType(Object propertyId);

    boolean supportsProperty(Object propertyId);

    /**
     * @return unmodifiable collection of items
     */
    Collection<I> getItems();

    /**
     * Update an item in the collection if it is already there.
     * <p>
     * Sends {@link ItemSetChangeEvent}.
     *
     * @param item the item to update
     */
    void updateItem(I item);

    /**
     * Registers a new value change listener.
     *
     * @param listener the listener to be added
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addValueChangeListener(Consumer<ValueChangeEvent<I>> listener);

    /**
     * Registers a new item set change listener.
     *
     * @param listener the listener to be added
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addItemSetChangeListener(Consumer<ItemSetChangeEvent<I>> listener);

    /**
     * Registers a new selected item change listener.
     *
     * @param listener the listener to be added
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addSelectedItemChangeListener(Consumer<SelectedItemChangeEvent<I>> listener);

    /**
     * Ordered table items.
     *
     * @param <T> row item type
     */
    interface Ordered<T> extends TableItems<T> {
        /**
         * Gets the ID of the item following the Item that corresponds to <code>itemId</code>.
         *
         * @param itemId item id
         * @return ID of the next visible Item or <code>null</code>
         */
        @Nullable
        Object nextItemId(Object itemId);

        /**
         * Gets the ID of the item preceding the item that corresponds to <code>itemId</code>.
         *
         * @param itemId item id
         * @return ID of the previous visible item or <code>null</code>
         */
        @Nullable
        Object prevItemId(Object itemId);

        /**
         * @return ID of the first visible item
         */
        @Nullable
        Object firstItemId();

        /**
         * @return ID of the last visible item
         */
        @Nullable
        Object lastItemId();

        /**
         * Tests if the Item corresponding to the given Item ID is the first item.
         *
         * @param itemId item id
         * @return <code>true</code> if the item is first visible item, <code>false</code> if not
         */
        boolean isFirstId(Object itemId);

        /**
         * Tests if the item corresponding to the given item ID is the last item.
         *
         * @param itemId item id
         * @return <code>true</code> if the item is last visible item in the, <code>false</code> if not
         */
        boolean isLastId(Object itemId);
    }

    /**
     * Sortable table items.
     *
     * @param <T> row item type
     */
    interface Sortable<T> extends Ordered<T> {
        /**
         * Sorts data.
         *
         * @param propertyId id of property
         * @param ascending asc / desc flags
         */
        void sort(Object[] propertyId, boolean[] ascending);

        /**
         * Resets sort order.
         */
        void resetSortOrder();

        default void suppressSorting() {
        }

        default void enableSorting(){
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

        public ValueChangeEvent(TableItems<T> source, T item, String property, @Nullable Object prevValue, @Nullable Object value) {
            super(source);
            this.item = item;
            this.property = property;
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public TableItems<T> getSource() {
            return (TableItems<T>) super.getSource();
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


        public Object getPrevValue() {
            return prevValue;
        }

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
        public ItemSetChangeEvent(TableItems<T> source) {
            super(source);
        }

        @SuppressWarnings("unchecked")
        @Override
        public TableItems<T> getSource() {
            return (TableItems<T>) super.getSource();
        }
    }

    /**
     * An event that is fired when selected item is changed.
     *
     * @param <T> row item type
     */
    class SelectedItemChangeEvent<T> extends EventObject {
        protected final T selectedItem;

        public SelectedItemChangeEvent(TableItems<T> source, @Nullable T selectedItem) {
            super(source);
            this.selectedItem = selectedItem;
        }

        @SuppressWarnings("unchecked")
        @Override
        public TableItems<T> getSource() {
            return (TableItems<T>) super.getSource();
        }

        @Nullable
        public T getSelectedItem() {
            return selectedItem;
        }
    }
}