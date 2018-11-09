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

package com.haulmont.cuba.web.gui.components.datagrid;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.vaadin.data.provider.AbstractDataProvider;
import com.vaadin.data.provider.DataChangeEvent;
import com.vaadin.data.provider.DataChangeEvent.DataRefreshEvent;
import com.vaadin.data.provider.Query;
import com.vaadin.server.SerializablePredicate;

import java.util.stream.Stream;

public class DataGridDataProvider<T> extends AbstractDataProvider<T, SerializablePredicate<T>> {

    protected DataGridItems<T> dataGridItems;
    protected DataGridItemsEventsDelegate<T> dataEventsDelegate;

    protected Subscription itemSetChangeSubscription;
    protected Subscription valueChangeSubscription;
    protected Subscription stateChangeSubscription;
    protected Subscription selectedItemChangeSubscription;

    public DataGridDataProvider(DataGridItems<T> dataGridItems,
                                DataGridItemsEventsDelegate<T> dataEventsDelegate) {
        this.dataGridItems = dataGridItems;
        this.dataEventsDelegate = dataEventsDelegate;

        this.itemSetChangeSubscription =
                this.dataGridItems.addItemSetChangeListener(this::datasourceItemSetChanged);
        this.valueChangeSubscription =
                this.dataGridItems.addValueChangeListener(this::datasourceValueChanged);
        this.stateChangeSubscription =
                this.dataGridItems.addStateChangeListener(this::datasourceStateChanged);
        this.selectedItemChangeSubscription =
                this.dataGridItems.addSelectedItemChangeListener(this::datasourceSelectedItemChanged);
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

    public DataGridItems<T> getDataGridItems() {
        return dataGridItems;
    }

    @Override
    public Object getId(T item) {
        return dataGridItems.getItemId(item);
    }

    @Override
    public boolean isInMemory() {
        return true;
    }

    @Override
    public int size(Query<T, SerializablePredicate<T>> query) {
        // FIXME: gg, query?
        if (dataGridItems.getState() == BindingState.INACTIVE) {
            return 0;
        }

        return dataGridItems.size();
    }

    @Override
    public Stream<T> fetch(Query<T, SerializablePredicate<T>> query) {
        if (dataGridItems.getState() == BindingState.INACTIVE) {
            return Stream.empty();
        }

        return dataGridItems.getItems()
                .skip(query.getOffset())
                .limit(query.getLimit());
    }

    protected void datasourceItemSetChanged(DataGridItems.ItemSetChangeEvent<T> event) {
        fireEvent(new DataChangeEvent<>(this));

        dataEventsDelegate.dataGridSourceItemSetChanged(event);
    }

    protected void datasourceValueChanged(DataGridItems.ValueChangeEvent<T> event) {
        fireEvent(new DataRefreshEvent<>(this, event.getItem()));

        dataEventsDelegate.dataGridSourcePropertyValueChanged(event);
    }

    protected void datasourceStateChanged(DataGridItems.StateChangeEvent event) {
        dataEventsDelegate.dataGridSourceStateChanged(event);
    }

    protected void datasourceSelectedItemChanged(DataGridItems.SelectedItemChangeEvent<T> event) {
        dataEventsDelegate.dataGridSourceSelectedItemChanged(event);
    }
}
