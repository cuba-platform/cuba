package com.haulmont.cuba.gui.components.data;

import com.haulmont.bali.events.Subscription;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A common interface for providing data for the {@link com.haulmont.cuba.gui.components.DataGrid} component.
 *
 * @param <T> items type
 */
public interface DataGridSource<T> {

    /**
     * @return a state of this source
     */
    BindingState getState();

    /**
     * @param item the item for obtaining the id
     * @return the id on the given item
     */
    Object getItemId(T item);

    /**
     * @param itemId the item id
     * @return the item by the given id
     */
    T getItem(@Nullable Object itemId);

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
     * Registers a new state change listener.
     *
     * @param listener the listener to be added
     * @return a registration object for removing an event listener added to a source
     */
    Subscription addStateChangeListener(Consumer<StateChangeEvent<T>> listener);

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
     * The DataGridSource that supports sorting.
     *
     * @param <T> items type
     */
    interface Sortable<T> extends DataGridSource<T> {

        void sort(Object[] propertyId, boolean[] ascending);

        void resetSortOrder();
    }

    /**
     * An event that is fired when DataGridSource state is changed.
     *
     * @param <T> the source component type
     */
    class StateChangeEvent<T> extends EventObject {
        protected BindingState state;

        public StateChangeEvent(DataGridSource<T> source, BindingState state) {
            super(source);
            this.state = state;
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataGridSource<T> getSource() {
            return (DataGridSource<T>) super.getSource();
        }

        public BindingState getState() {
            return state;
        }
    }

    /**
     * An event that is fired when DataGridSource value is changed.
     *
     * @param <T> the source component type
     */
    class ValueChangeEvent<T> extends EventObject {
        private final T item;
        private final String property;
        private final Object prevValue;
        private final Object value;

        public ValueChangeEvent(DataGridSource<T> source, T item, String property, Object prevValue, Object value) {
            super(source);
            this.item = item;
            this.property = property;
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataGridSource<T> getSource() {
            return (DataGridSource<T>) super.getSource();
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
     * An event that is fired when DataGridSource item set is changed.
     *
     * @param <T> the source component type
     */
    class ItemSetChangeEvent<T> extends EventObject {
        public ItemSetChangeEvent(DataGridSource<T> source) {
            super(source);
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataGridSource<T> getSource() {
            return (DataGridSource<T>) super.getSource();
        }
    }

    /**
     * An event that is fired when DataGridSource selected item is changed.
     *
     * @param <T> the source component type
     */
    class SelectedItemChangeEvent<T> extends EventObject {
        protected final T selectedItem;

        public SelectedItemChangeEvent(DataGridSource<T> source, T selectedItem) {
            super(source);
            this.selectedItem = selectedItem;
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataGridSource<T> getSource() {
            return (DataGridSource<T>) super.getSource();
        }

        /**
         * @return a new selected item
         */
        public T getSelectedItem() {
            return selectedItem;
        }
    }
}
