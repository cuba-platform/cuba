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

package com.haulmont.cuba.gui.components.data.value;

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Sort;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.EntityTableSource;
import com.haulmont.cuba.gui.components.data.TableSource;
import com.haulmont.cuba.gui.model.CollectionContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CollectionContainerTableSource<E extends Entity> implements EntityTableSource<E>, TableSource.Sortable<E> {

    protected CollectionContainer<E> container;

    protected BindingState state = BindingState.ACTIVE;

    protected EventHub events = new EventHub();

    public CollectionContainerTableSource(CollectionContainer<E> container) {
        this.container = container;
        this.container.addItemChangeListener(this::containerItemChanged);
        this.container.addCollectionChangeListener(this::containerCollectionChanged);
        this.container.addItemPropertyChangeListener(this::containerItemPropertyChanged);
    }

    protected void containerItemChanged(CollectionContainer.ItemChangeEvent<E> event) {
        events.publish(SelectedItemChangeEvent.class, new SelectedItemChangeEvent<>(this, event.getItem()));
    }

    protected void containerCollectionChanged(CollectionContainer.CollectionChangeEvent<E> e) {
        events.publish(ItemSetChangeEvent.class, new ItemSetChangeEvent<>(this));
    }

    @SuppressWarnings("unchecked")
    protected void containerItemPropertyChanged(CollectionContainer.ItemPropertyChangeEvent<E> e) {
        events.publish(ValueChangeEvent.class, new ValueChangeEvent(this,
                e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue()));
    }

    @Override
    public Collection<?> getItemIds() {
        return container.getItems().stream().map(Entity::getId).collect(Collectors.toList());
    }

    @Nullable
    @Override
    public E getItem(Object itemId) {
        return container.getItemOrNull(itemId);
    }

    @Override
    public E getItemNN(Object itemId) {
        return container.getItem(itemId);
    }

    @Override
    public Object getItemValue(Object itemId, Object propertyId) {
        MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;
        return container.getItem(itemId).getValueEx(propertyPath.toPathString());
    }

    @Override
    public int size() {
        return container.getItems().size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return container.getItems().stream().anyMatch(e -> e.getId().equals(itemId));
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

    @Nullable
    @Override
    public E getSelectedItem() {
        return container.getItemOrNull();
    }

    @Override
    public void setSelectedItem(@Nullable E item) {
        container.setItem(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent<E>> listener) {
        return events.subscribe(StateChangeEvent.class, (Consumer)listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<E>> listener) {
        return events.subscribe(ValueChangeEvent.class, (Consumer)listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addItemSetChangeListener(Consumer<ItemSetChangeEvent<E>> listener) {
        return events.subscribe(ItemSetChangeEvent.class, (Consumer)listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addSelectedItemChangeListener(Consumer<SelectedItemChangeEvent<E>> listener) {
        return events.subscribe(SelectedItemChangeEvent.class, (Consumer)listener);
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return container.getEntityMetaClass();
    }

    @Override
    public Collection<MetaPropertyPath> getAutowiredProperties() {
        MetadataTools metadataTools = AppBeans.get(MetadataTools.class);

        return container.getView() != null ?
                // if a view is specified - use view properties
                metadataTools.getViewPropertyPaths(container.getView(), container.getEntityMetaClass()) :
                // otherwise use all properties from meta-class
                metadataTools.getPropertyPaths(container.getEntityMetaClass());
    }

    @Override
    public Object nextItemId(Object itemId) {
        List<E> items = container.getItems();
        int index = container.getItemIndex(itemId);
        return index == items.size() - 1 ? null : items.get(index + 1).getId();
    }

    @Override
    public Object prevItemId(Object itemId) {
        int index = container.getItemIndex(itemId);
        return index <= 0 ? null : container.getItems().get(index - 1).getId();
    }

    @Override
    public Object firstItemId() {
        List<E> items = container.getItems();
        return items.isEmpty() ? null : items.get(0).getId();
    }

    @Override
    public Object lastItemId() {
        List<E> items = container.getItems();
        return items.isEmpty() ? null : items.get(0).getId();
    }

    @Override
    public boolean isFirstId(Object itemId) {
        int index = container.getItemIndex(itemId);
        return index == 0;
    }

    @Override
    public boolean isLastId(Object itemId) {
        int index = container.getItemIndex(itemId);
        return index == container.getItems().size() - 1;
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
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
        container.getSorter().sort(Sort.by(orders));
    }

    @Override
    public void resetSortOrder() {
        container.getSorter().sort(Sort.UNSORTED);
    }
}