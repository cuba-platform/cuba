package com.haulmont.cuba.web.gui.components.datagrid;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.DataGridSource;
import com.vaadin.data.provider.AbstractDataProvider;
import com.vaadin.data.provider.DataChangeEvent;
import com.vaadin.data.provider.DataChangeEvent.DataRefreshEvent;
import com.vaadin.data.provider.Query;
import com.vaadin.server.SerializablePredicate;

import java.util.stream.Stream;

public class DataGridDataProvider<T> extends AbstractDataProvider<T, SerializablePredicate<T>> {

    protected DataGridSource<T> dataGridSource;
    protected DataGridSourceEventsDelegate<T> dataEventsDelegate;

    protected Subscription itemSetChangeSubscription;
    protected Subscription valueChangeSubscription;
    protected Subscription stateChangeSubscription;
    protected Subscription selectedItemChangeSubscription;

    public DataGridDataProvider(DataGridSource<T> dataGridSource,
                                DataGridSourceEventsDelegate<T> dataEventsDelegate) {
        this.dataGridSource = dataGridSource;
        this.dataEventsDelegate = dataEventsDelegate;

        this.itemSetChangeSubscription =
                this.dataGridSource.addItemSetChangeListener(this::datasourceItemSetChanged);
        this.valueChangeSubscription =
                this.dataGridSource.addValueChangeListener(this::datasourceValueChanged);
        this.stateChangeSubscription =
                this.dataGridSource.addStateChangeListener(this::datasourceStateChanged);
        this.selectedItemChangeSubscription =
                this.dataGridSource.addSelectedItemChangeListener(this::datasourceSelectedItemChanged);
    }

    public void unbind() {
        if (itemSetChangeSubscription != null) {
            this.itemSetChangeSubscription.remove();
            this.itemSetChangeSubscription = null;
        }

        if (valueChangeSubscription != null) {
            this.valueChangeSubscription.remove();
            this.valueChangeSubscription = null;
        }

        if (stateChangeSubscription != null) {
            this.stateChangeSubscription.remove();
            this.stateChangeSubscription = null;
        }

        if (selectedItemChangeSubscription != null) {
            this.selectedItemChangeSubscription.remove();
            this.selectedItemChangeSubscription = null;
        }
    }

    public DataGridSource<T> getDataGridSource() {
        return dataGridSource;
    }

    @Override
    public Object getId(T item) {
        return dataGridSource.getItemId(item);
    }

    @Override
    public boolean isInMemory() {
        return true;
    }

    @Override
    public int size(Query<T, SerializablePredicate<T>> query) {
        // TODO: gg, query?
        if (dataGridSource.getState() == BindingState.INACTIVE) {
            return 0;
        }

        return dataGridSource.size();
    }

    @Override
    public Stream<T> fetch(Query<T, SerializablePredicate<T>> query) {
        if (dataGridSource.getState() == BindingState.INACTIVE) {
            return Stream.empty();
        }

        return dataGridSource.getItems()
                .skip(query.getOffset())
                .limit(query.getLimit());
    }

    protected void datasourceItemSetChanged(DataGridSource.ItemSetChangeEvent<T> event) {
        fireEvent(new DataChangeEvent<>(this));

        dataEventsDelegate.dataGridSourceItemSetChanged(event);
    }

    protected void datasourceValueChanged(DataGridSource.ValueChangeEvent<T> event) {
        fireEvent(new DataRefreshEvent<>(this, event.getItem()));

        dataEventsDelegate.dataGridSourcePropertyValueChanged(event);
    }

    protected void datasourceStateChanged(DataGridSource.StateChangeEvent<T> event) {
        dataEventsDelegate.dataGridSourceStateChanged(event);
    }

    protected void datasourceSelectedItemChanged(DataGridSource.SelectedItemChangeEvent<T> event) {
        dataEventsDelegate.dataGridSourceSelectedItemChanged(event);
    }
}
