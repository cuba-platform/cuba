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

package com.haulmont.cuba.gui.components.data.table;

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.AggregatableTableItems;
import com.haulmont.cuba.gui.components.data.meta.EntityTableItems;
import com.haulmont.cuba.gui.components.data.meta.DatasourceDataUnit;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class DatasourceTableItems<E extends Entity<K>, K>
        implements EntityTableItems<E>, DatasourceDataUnit, AggregatableTableItems<E> {

    protected CollectionDatasource datasource;
    protected EventHub events = new EventHub();

    protected BindingState state = BindingState.INACTIVE;

    public DatasourceTableItems(CollectionDatasource<E, K> datasource) {
        this.datasource = datasource;

        this.datasource.addStateChangeListener(this::datasourceStateChanged);
        this.datasource.addItemPropertyChangeListener(this::datasourceItemPropertyChanged);
        this.datasource.addCollectionChangeListener(this::datasourceCollectionChanged);
        this.datasource.addItemChangeListener(this::datasourceItemChanged);

        CollectionDsHelper.autoRefreshInvalid(datasource, true);

        if (datasource.getState() == Datasource.State.VALID) {
            setState(BindingState.ACTIVE);
        }
    }

    protected void datasourceItemChanged(Datasource.ItemChangeEvent event) {
        events.publish(SelectedItemChangeEvent.class, new SelectedItemChangeEvent<>(this, (E)event.getItem()));
    }

    protected void datasourceCollectionChanged(@SuppressWarnings("unused") CollectionDatasource.CollectionChangeEvent<E, K> e) {
        events.publish(ItemSetChangeEvent.class, new ItemSetChangeEvent<>(this));
    }

    @SuppressWarnings("unchecked")
    protected void datasourceItemPropertyChanged(Datasource.ItemPropertyChangeEvent<E> e) {
        events.publish(ValueChangeEvent.class, new ValueChangeEvent(this,
                e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue()));
    }

    protected void datasourceStateChanged(Datasource.StateChangeEvent<E> e) {
        if (e.getState() == Datasource.State.VALID) {
            setState(BindingState.ACTIVE);
        } else {
            setState(BindingState.INACTIVE);
        }
    }

    public void setState(BindingState state) {
        if (this.state != state) {
            this.state = state;

            events.publish(StateChangeEvent.class, new StateChangeEvent(this, state));
        }
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public Collection<?> getItemIds() {
        return datasource.getItemIds();
    }

    @Override
    public E getSelectedItem() {
        return (E) datasource.getItem();
    }

    @Override
    public void setSelectedItem(@Nullable E item) {
        datasource.setItem(item);
    }

    @Override
    public E getItem(Object itemId) {
        return (E) datasource.getItem(itemId);
    }

    @Override
    public Object getItemValue(Object itemId, Object propertyId) {
        MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;
        return datasource.getItemNN(itemId).getValueEx(propertyPath);
    }

    @Override
    public void updateItem(E item) {
        datasource.updateItem(item);
    }

    @Override
    public int size() {
        return datasource.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        //noinspection unchecked
        return datasource.containsItem(itemId);
    }

    @Override
    public BindingState getState() {
        return state;
    }

    @Override
    public Class<?> getType(Object propertyId) {
        MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;
        return propertyPath.getRangeJavaClass();
    }

    @Override
    public boolean supportsProperty(Object propertyId) {
        return propertyId instanceof MetaPropertyPath;
    }

    @Override
    public Collection<E> getItems() {
        return datasource.getItems();
    }

    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent> listener) {
        return events.subscribe(StateChangeEvent.class, listener);
    }

    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<E>> listener) {
        return events.subscribe(ValueChangeEvent.class, (Consumer)listener);
    }

    @Override
    public Subscription addItemSetChangeListener(Consumer<ItemSetChangeEvent<E>> listener) {
        return events.subscribe(ItemSetChangeEvent.class, (Consumer)listener);
    }

    @Override
    public Subscription addSelectedItemChangeListener(Consumer<SelectedItemChangeEvent<E>> listener) {
        return events.subscribe(SelectedItemChangeEvent.class, (Consumer)listener);
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return datasource.getMetaClass();
    }

    @Override
    public Map<AggregationInfo, String> aggregate(AggregationInfo[] aggregationInfos, Collection<?> itemIds) {
        return ((CollectionDatasource.Aggregatable) datasource).aggregate(aggregationInfos, itemIds);
    }

    @Override
    public Map<AggregationInfo, Object> aggregateValues(AggregationInfo[] aggregationInfos, Collection<?> itemIds) {
        return ((CollectionDatasource.Aggregatable) datasource).aggregateValues(aggregationInfos, itemIds);
    }
}