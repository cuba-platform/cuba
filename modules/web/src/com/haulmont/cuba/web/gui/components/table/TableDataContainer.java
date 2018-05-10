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

package com.haulmont.cuba.web.gui.components.table;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.TableDataSource;
import com.haulmont.cuba.web.gui.data.StaticItemSetChangeEvent;
import com.vaadin.ui.UI;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Container.ItemSetChangeNotifier;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;

import java.util.*;

@SuppressWarnings("deprecation")
public class TableDataContainer<I> implements Container, ItemSetChangeNotifier {
    protected static final Property.ValueChangeEvent VOID_VALUE_CHANGE_EVENT = () -> null;

    protected TableDataSource<I> tableDataSource;
    protected boolean ignoreListeners;

    protected Collection<Object> properties = new ArrayList<>();

    protected Map<Object, TableItemWrapper> itemsCache = new HashMap<>();

    // Reusable table item wrappers
    protected Queue<TableItemWrapper> wrappersPool = new ArrayDeque<>(50);

    protected List<ItemSetChangeListener> itemSetChangeListeners = new ArrayList<>(2);
    protected List<Property.ValueChangeListener> propertyValueChangeListeners = new ArrayList<>(2);

    protected Subscription itemSetChangeSubscription;
    protected Subscription valueChangeSubscription;
    protected Subscription stateChangeSubscription;

    public TableDataContainer(TableDataSource<I> tableDataSource) {
        this.tableDataSource = tableDataSource;

        this.itemSetChangeSubscription = this.tableDataSource.addItemSetChangeListener(this::datasourceItemSetChanged);
        this.valueChangeSubscription = this.tableDataSource.addValueChangeListener(this::datasourceValueChanged);
        this.stateChangeSubscription = this.tableDataSource.addStateChangeListener(this::datasourceStateChanged);
    }

    public void setProperties(Collection<Object> properties) {
        this.properties.clear();
        this.properties.addAll(properties);
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
    }

    @Override
    public Item getItem(Object itemId) {
        Object dataItem = tableDataSource.getItem(itemId);
        return dataItem == null ? null : getItemWrapper(dataItem);
    }

    protected Item getItemWrapper(Object item) {
        TableItemWrapper wrapper = itemsCache.get(item);
        if (wrapper == null) {
            wrapper = getItemWrapperNonCached(item);
            itemsCache.put(item, wrapper);
        }

        return wrapper;
    }

    protected TableItemWrapper borrowItemWrapper() {
        if (wrappersPool.isEmpty()) {
            return new TableItemWrapper(this);
        } else {
            return wrappersPool.poll();
        }
    }

    protected void returnItemWrapper(TableItemWrapper tableItemWrapper) {
        tableItemWrapper.setItem(null);
        tableItemWrapper.getPropertyWrappers().clear();
        wrappersPool.add(tableItemWrapper);
    }

    protected TableItemWrapper getItemWrapperNonCached(Object item) {
        TableItemWrapper itemWrapper = borrowItemWrapper();
        itemWrapper.setItem(item);
        for (Object property : properties) {
            itemWrapper.getPropertyWrappers().put(property,
                    new TableItemPropertyWrapper(itemWrapper, property));
        }
        return itemWrapper;
    }

    protected void fireItemSetChanged() {
        if (ignoreListeners) {
            return;
        }

        ignoreListeners = true;

        if (UI.getCurrent().getConnectorTracker().isWritingResponse()) {
            // Suppress containerItemSetChange listeners during painting, undefined behavior may be occurred
            return;
        }

        StaticItemSetChangeEvent event = new StaticItemSetChangeEvent(this);
        for (ItemSetChangeListener listener : itemSetChangeListeners) {
            listener.containerItemSetChange(event);
        }
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return properties;
    }

    @Override
    public Collection<?> getItemIds() {
        return tableDataSource.getItemIds();
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        final Item item = getItem(itemId);
        return item == null ? null : item.getItemProperty(propertyId);
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return tableDataSource.getType(propertyId);
    }

    @Override
    public int size() {
        return tableDataSource.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return tableDataSource.containsId(itemId);
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
            throws UnsupportedOperationException {
        if (tableDataSource.supportsProperty(propertyId)) {
            return this.properties.add(propertyId);
        }
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        return this.properties.remove(propertyId);
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        if (!itemSetChangeListeners.contains(listener)) {
            itemSetChangeListeners.add(listener);
        }
    }

    @Override
    public void addListener(ItemSetChangeListener listener) {
        addItemSetChangeListener(listener);
    }

    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {
        itemSetChangeListeners.remove(listener);
    }

    @Override
    public void removeListener(ItemSetChangeListener listener) {
        removeItemSetChangeListener(listener);
    }

    protected void datasourceItemSetChanged(TableDataSource.ItemSetChangeEvent<I> e) {
        for (TableItemWrapper itemWrapper : itemsCache.values()) {
            returnItemWrapper(itemWrapper);
        }
        itemsCache.clear();

        boolean prevIgnoreListeners = ignoreListeners;
        try {
            fireItemSetChanged();
        } finally {
            ignoreListeners = prevIgnoreListeners;
        }

        // todo call Table delegate
    }

    protected void datasourceValueChanged(TableDataSource.ValueChangeEvent<I> e) {
        for (Property.ValueChangeListener listener : propertyValueChangeListeners) {
            // table implementation does not use property of value change event
            // see /com/vaadin/v7/ui/Table.java:valueChange(Property.ValueChangeEvent event)
            listener.valueChange(VOID_VALUE_CHANGE_EVENT);
        }

        // todo call Table delegate
    }

    protected void datasourceStateChanged(TableDataSource.StateChangeEvent<I> e) {
        if (e.getState() == BindingState.INACTIVE) {
            for (TableItemWrapper itemWrapper : itemsCache.values()) {
                returnItemWrapper(itemWrapper);
            }
            itemsCache.clear();
        }

        // todo call Table delegate
    }

    public TableDataSource<I> getTableDataSource() {
        return tableDataSource;
    }

    public void addValueChangeListener(Property.ValueChangeListener propertyValueChangeListener) {
        if (!this.propertyValueChangeListeners.contains(propertyValueChangeListener)) {
            propertyValueChangeListeners.add(propertyValueChangeListener);
        }
    }
}