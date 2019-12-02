/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.components.data.datagrid;

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Sort;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.components.data.AggregatableDataGridItems;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityDataGridItems;
import com.haulmont.cuba.gui.data.impl.AggregatableDelegate;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.HasLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ContainerDataGridItems<E extends Entity>
        implements EntityDataGridItems<E>, AggregatableDataGridItems<E>, DataGridItems.Sortable<E>, ContainerDataUnit<E> {

    private static final Logger log = LoggerFactory.getLogger(ContainerDataGridItems.class);

    protected CollectionContainer<E> container;

    protected boolean suppressSorting;

    protected AggregatableDelegate aggregatableDelegate;

    protected EventHub events = new EventHub();

    public ContainerDataGridItems(CollectionContainer<E> container) {
        this.container = container;
        this.container.addItemChangeListener(this::containerItemChanged);
        this.container.addCollectionChangeListener(this::containerCollectionChanged);
        this.container.addItemPropertyChangeListener(this::containerItemPropertyChanged);

        this.aggregatableDelegate = createAggregatableDelegate();
    }

    @Override
    public CollectionContainer<E> getContainer() {
        return container;
    }

    protected void containerItemChanged(CollectionContainer.ItemChangeEvent<E> event) {
        events.publish(DataGridItems.SelectedItemChangeEvent.class, new DataGridItems.SelectedItemChangeEvent<>(this, event.getItem()));
    }

    protected void containerCollectionChanged(@SuppressWarnings("unused") CollectionContainer.CollectionChangeEvent<E> e) {
        events.publish(DataGridItems.ItemSetChangeEvent.class, new DataGridItems.ItemSetChangeEvent<>(this));
    }

    @SuppressWarnings("unchecked")
    protected void containerItemPropertyChanged(CollectionContainer.ItemPropertyChangeEvent<E> e) {
        events.publish(DataGridItems.ValueChangeEvent.class, new DataGridItems.ValueChangeEvent(this,
                e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue()));
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return container.getEntityMetaClass();
    }

    @Override
    public BindingState getState() {
        return BindingState.ACTIVE;
    }

    @Override
    public Object getItemId(E item) {
        return item.getId();
    }

    @Override
    public E getItem(@Nullable Object itemId) {
        return itemId == null ? null : container.getItemOrNull(itemId);
    }

    @Override
    public int indexOfItem(E item) {
        return container.getItemIndex(item.getId());
    }

    @Nullable
    @Override
    public E getItemByIndex(int index) {
        return container.getItems().get(index);
    }

    @Override
    public Stream<E> getItems() {
        return container.getItems().stream();
    }

    @Override
    public List<E> getItems(int startIndex, int numberOfItems) {
        return container.getItems().subList(startIndex, startIndex + numberOfItems);
    }

    @Override
    public boolean containsItem(E item) {
        return container.getItemOrNull(item.getId()) != null;
    }

    @Override
    public int size() {
        return container.getItems().size();
    }

    @Nullable
    @Override
    public E getSelectedItem() {
        return container.getItemOrNull();
    }

    @Override
    public void setSelectedItem(@Nullable E item) {
        container.setItem(item);
    }

    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent> listener) {
        return events.subscribe(StateChangeEvent.class, listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<E>> listener) {
        return events.subscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addItemSetChangeListener(Consumer<ItemSetChangeEvent<E>> listener) {
        return events.subscribe(ItemSetChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addSelectedItemChangeListener(Consumer<SelectedItemChangeEvent<E>> listener) {
        return events.subscribe(SelectedItemChangeEvent.class, (Consumer) listener);
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        if (container.getSorter() != null) {
            if (suppressSorting && container instanceof HasLoader && ((HasLoader) container).getLoader() instanceof CollectionLoader) {
                ((CollectionLoader) ((HasLoader) container).getLoader()).setSort(createSort(propertyId, ascending));
            } else {
                container.getSorter().sort(createSort(propertyId, ascending));
            }
        } else {
            log.debug("Container {} sorter is null", container);
        }
    }

    protected Sort createSort(Object[] propertyId, boolean[] ascending) {
        List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < propertyId.length; i++) {
            String property;
            if (propertyId[i] instanceof MetaPropertyPath) {
                property = ((MetaPropertyPath) propertyId[i]).toPathString();
            } else {
                property = (String) propertyId[i];
            }
            Sort.Order order = ascending[i] ? Sort.Order.asc(property) : Sort.Order.desc(property);
            orders.add(order);
        }
        return Sort.by(orders);
    }

    @Override
    public void resetSortOrder() {
        if (container.getSorter() != null) {
            if (suppressSorting && container instanceof HasLoader && ((HasLoader) container).getLoader() instanceof CollectionLoader) {
                ((CollectionLoader) ((HasLoader) container).getLoader()).setSort(Sort.UNSORTED);
            } else {
                container.getSorter().sort(Sort.UNSORTED);
            }
        } else {
            log.debug("Container {} sorter is null", container);
        }
    }

    @Override
    public void suppressSorting() {
        suppressSorting = true;
    }

    @Override
    public void enableSorting() {
        suppressSorting = false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<AggregationInfo, String> aggregate(AggregationInfo[] aggregationInfos, Collection<?> itemIds) {
        return aggregatableDelegate.aggregate(aggregationInfos, itemIds);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<AggregationInfo, Object> aggregateValues(AggregationInfo[] aggregationInfos, Collection<?> itemIds) {
        return aggregatableDelegate.aggregateValues(aggregationInfos, itemIds);
    }

    protected AggregatableDelegate createAggregatableDelegate() {
        return new AggregatableDelegate() {
            @Override
            public Object getItem(Object itemId) {
                return container.getItem(itemId);
            }

            @Override
            public Object getItemValue(MetaPropertyPath property, Object itemId) {
                return container.getItem(itemId).getValueEx(property);
            }
        };
    }
}