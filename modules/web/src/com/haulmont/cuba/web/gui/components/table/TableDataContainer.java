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
import com.haulmont.cuba.gui.components.data.TableItems;
import com.haulmont.cuba.web.gui.data.StaticItemSetChangeEvent;
import com.vaadin.ui.UI;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Container.ItemSetChangeNotifier;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.AbstractProperty;

import java.util.*;

@SuppressWarnings("deprecation")
public class TableDataContainer<I> implements Container, ItemSetChangeNotifier {

    protected static final Property.ValueChangeEvent VOID_VALUE_CHANGE_EVENT = () -> new AbstractProperty() {
        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void setValue(Object newValue) throws ReadOnlyException {
        }

        @Override
        public Class getType() {
            return Object.class;
        }
    };

    protected TableItems<I> tableItems;
    protected TableItemsEventsDelegate<I> dataEventsDelegate;
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
    protected Subscription selectedItemChangeSubscription;

    public TableDataContainer(TableItems<I> tableItems, TableItemsEventsDelegate<I> dataEventsDelegate) {
        this.tableItems = tableItems;
        this.dataEventsDelegate = dataEventsDelegate;

        this.itemSetChangeSubscription = this.tableItems.addItemSetChangeListener(this::datasourceItemSetChanged);
        this.valueChangeSubscription = this.tableItems.addValueChangeListener(this::datasourceValueChanged);
        this.stateChangeSubscription = this.tableItems.addStateChangeListener(this::datasourceStateChanged);
        this.selectedItemChangeSubscription = this.tableItems.addSelectedItemChangeListener(this::datasourceSelectedItemChanged);
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
        if (selectedItemChangeSubscription != null) {
            this.selectedItemChangeSubscription.remove();
            this.selectedItemChangeSubscription = null;
        }
        wrappersPool.clear();
        resetCachedItems();
    }

    @Override
    public Item getItem(Object itemId) {
        Object dataItem = tableItems.getItem(itemId);
        return dataItem == null ? null : getItemWrapper(dataItem, itemId);
    }

    public I getInternalItem(Object itemId) {
        return tableItems.getItem(itemId);
    }

    protected Item getItemWrapper(Object dataItem, Object itemId) {
        TableItemWrapper wrapper = itemsCache.get(dataItem);
        if (wrapper == null) {
            wrapper = getItemWrapperNonCached(dataItem, itemId);
            itemsCache.put(dataItem, wrapper);
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
        tableItemWrapper.setItemId(null);
        tableItemWrapper.getPropertyWrappers().clear();
        wrappersPool.add(tableItemWrapper);
    }

    protected TableItemWrapper getItemWrapperNonCached(@SuppressWarnings("unused") Object item, Object itemId) {
        TableItemWrapper itemWrapper = borrowItemWrapper();
        itemWrapper.setItemId(itemId);
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

        UI ui = UI.getCurrent();
        if (ui != null && ui.getConnectorTracker().isWritingResponse()) {
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
        if (tableItems.getState() == BindingState.INACTIVE) {
            return Collections.emptyList();
        }

        return tableItems.getItemIds();
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        final Item item = getItem(itemId);
        return item == null ? null : item.getItemProperty(propertyId);
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return tableItems.getType(propertyId);
    }

    @Override
    public int size() {
        if (tableItems.getState() == BindingState.INACTIVE) {
            return 0;
        }

        return tableItems.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return tableItems.containsId(itemId);
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
        if (tableItems.supportsProperty(propertyId)) {
            return this.properties.add(propertyId);
        }
        throw new UnsupportedOperationException();
    }

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

    protected void resetCachedItems() {
        itemsCache.clear();
    }

    protected void datasourceItemSetChanged(TableItems.ItemSetChangeEvent<I> e) {
        for (TableItemWrapper itemWrapper : itemsCache.values()) {
            returnItemWrapper(itemWrapper);
        }
        resetCachedItems();

        beforeFireItemSetChanged();

        boolean prevIgnoreListeners = ignoreListeners;
        try {
            fireItemSetChanged();
        } finally {
            ignoreListeners = prevIgnoreListeners;
        }

        dataEventsDelegate.tableSourceItemSetChanged(e);
    }

    protected void beforeFireItemSetChanged() {
        // can be overridden in descendants
    }

    protected void datasourceValueChanged(TableItems.ValueChangeEvent<I> e) {
        for (Property.ValueChangeListener listener : propertyValueChangeListeners) {
            // table implementation does not use property of value change event
            // see /com/vaadin/v7/ui/Table.java:valueChange(Property.ValueChangeEvent event)
            listener.valueChange(VOID_VALUE_CHANGE_EVENT);
        }

        dataEventsDelegate.tableSourcePropertyValueChanged(e);
    }

    protected void beforeFireStateChanged() {
        // can be overridden in descendants
    }

    protected void datasourceStateChanged(TableItems.StateChangeEvent e) {
        if (e.getState() == BindingState.INACTIVE) {
            for (TableItemWrapper itemWrapper : itemsCache.values()) {
                returnItemWrapper(itemWrapper);
            }
            resetCachedItems();
        }

        beforeFireStateChanged();

        dataEventsDelegate.tableSourceStateChanged(e);
    }

    protected void datasourceSelectedItemChanged(TableItems.SelectedItemChangeEvent<I> e) {
        dataEventsDelegate.tableSourceSelectedItemChanged(e);
    }

    public TableItems<I> getTableItems() {
        return tableItems;
    }

    public void addValueChangeListener(Property.ValueChangeListener propertyValueChangeListener) {
        if (!this.propertyValueChangeListeners.contains(propertyValueChangeListener)) {
            propertyValueChangeListeners.add(propertyValueChangeListener);
        }
    }
}