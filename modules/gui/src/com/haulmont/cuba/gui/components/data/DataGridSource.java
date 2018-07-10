package com.haulmont.cuba.gui.components.data;

import com.haulmont.bali.events.Subscription;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * todo JavaDoc
 *
 * @param <T>
 */
public interface DataGridSource<T> {

    BindingState getState();

    Object getItemId(T item);

    T getItem(@Nullable Object itemId);

    int indexOfItem(T item);

    @Nullable
    T getItemByIndex(int index);

    Stream<T> getItems();

    // TODO: gg, probably we don't need this method
    List<T> getItems(int startIndex, int numberOfItems);

    boolean containsItem(T item);

    int size();

    @Nullable
    T getSelectedItem();
    void setSelectedItem(@Nullable T item);

    Subscription addStateChangeListener(Consumer<StateChangeEvent<T>> listener);
    Subscription addValueChangeListener(Consumer<ValueChangeEvent<T>> listener);
    Subscription addItemSetChangeListener(Consumer<ItemSetChangeEvent<T>> listener);
    Subscription addSelectedItemChangeListener(Consumer<SelectedItemChangeEvent<T>> listener);

    interface Sortable<T> extends DataGridSource<T> {
        void sort(Object[] propertyId, boolean[] ascending);

        void resetSortOrder();
    }

    // todo
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

    // todo
    class ValueChangeEvent<T> extends EventObject {
        private final T item;
        private final Object prevValue;
        private final Object value;

        public ValueChangeEvent(DataGridSource<T> source, T item, Object prevValue, Object value) {
            super(source);
            this.item = item;
            this.prevValue = prevValue;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataGridSource<T> getSource() {
            return (DataGridSource<T>) super.getSource();
        }

        public T getItem() {
            return item;
        }

        public Object getPrevValue() {
            return prevValue;
        }

        public Object getValue() {
            return value;
        }
    }

    // todo
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

    // todo
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

        public T getSelectedItem() {
            return selectedItem;
        }
    }
}
