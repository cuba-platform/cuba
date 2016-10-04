/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.*;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Uses any Object as his Id
 */
public class OptionsDsWrapper implements Container.Ordered, Container.ItemSetChangeNotifier, UnsubscribableDsWrapper {

    protected boolean autoRefresh;
    protected boolean ignoreListeners;
    protected boolean executeAutoRefreshInvalid = true;

    protected CollectionDatasource datasource;

    protected Datasource.StateChangeListener dsStateChangeListener;
    protected Datasource.ItemPropertyChangeListener dsItemPropertyChangeListener;
    protected CollectionDatasource.CollectionChangeListener cdsCollectionChangeListener;

    protected WeakDsListenerAdapter weakDsListenerAdapter;

    protected Collection<MetaPropertyPath> properties = new ArrayList<>();

    protected Map<Object, ItemWrapper> itemsCache = new HashMap<>();

    // lazily initialized listeners list
    protected List<ItemSetChangeListener> itemSetChangeListeners = null;

    public OptionsDsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
        this(datasource, null, autoRefresh);
    }

    @SuppressWarnings("unchecked")
    public OptionsDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties, boolean autoRefresh) {
        this.datasource = datasource;
        this.autoRefresh = autoRefresh;

        final View view = datasource.getView();
        final MetaClass metaClass = datasource.getMetaClass();

        if (properties == null) {
            createProperties(view, metaClass);
        } else {
            this.properties.addAll(properties);
        }

        dsStateChangeListener = e -> itemsCache.clear();

        dsItemPropertyChangeListener = e -> {
            Item wrapper = getItemWrapper(e.getItem());

            // MetaProperty worked wrong with properties from inherited superclasses
            MetaPropertyPath metaPropertyPath = datasource.getMetaClass().getPropertyPath(e.getProperty());
            if (metaPropertyPath == null) {
                return;
            }
            Property itemProperty = wrapper.getItemProperty(metaPropertyPath);
            if (itemProperty instanceof PropertyWrapper) {
                ((PropertyWrapper) itemProperty).fireValueChangeEvent();
            }
        };

        cdsCollectionChangeListener = e -> {
            final boolean prevIgnoreListeners = ignoreListeners;
            try {
                itemsCache.clear();
                fireItemSetChanged();
            } finally {
                ignoreListeners = prevIgnoreListeners;
            }
        };

        weakDsListenerAdapter = new WeakDsListenerAdapter(datasource, dsItemPropertyChangeListener, dsStateChangeListener, cdsCollectionChangeListener);
        datasource.addCollectionChangeListener(weakDsListenerAdapter);
        datasource.addStateChangeListener(weakDsListenerAdapter);
        datasource.addItemPropertyChangeListener(weakDsListenerAdapter);
    }

    protected void createProperties(View view, MetaClass metaClass) {
        properties.addAll(CollectionDsHelper.createProperties(view, metaClass));
    }

    protected void fireItemSetChanged() {
        if (ignoreListeners) {
            return;
        }

        ignoreListeners = true;

        UI currentUi = UI.getCurrent();
        if (currentUi != null) {
            // ignore in tests
            if (currentUi.getConnectorTracker().isWritingResponse()) {
                // Suppress containerItemSetChange listeners during painting, undefined behavior may be occurred
                return;
            }
        }

        if (itemSetChangeListeners != null) {
            StaticItemSetChangeEvent event = new StaticItemSetChangeEvent(this);

            for (ItemSetChangeListener listener : itemSetChangeListeners) {
                listener.containerItemSetChange(event);
            }
        }

        ignoreListeners = false;
    }

    @Override
    public Item getItem(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);

        Entity entity = (Entity) itemId;
        //noinspection unchecked
        final Object item = datasource.getItem(entity.getId());
        return item == null ? null : getItemWrapper(item);
    }

    protected Item getItemWrapper(Object item) {
        ItemWrapper wrapper = itemsCache.get(item);
        if (wrapper == null) {
            wrapper = createItemWrapper(item);
            itemsCache.put(item, wrapper);
        }

        return wrapper;
    }

    protected ItemWrapper createItemWrapper(Object item) {
        return new ItemWrapper(item, datasource.getMetaClass(), properties);
    }

    @Override
    public Collection getContainerPropertyIds() {
        return properties;
    }

    @Override
    public Collection getItemIds() {
        if (executeAutoRefreshInvalid) {
            if (UI.getCurrent().getConnectorTracker().isWritingResponse()) {
                try {
                    CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
                } catch (IllegalStateException ex) {
                    if (StringUtils.contains(ex.getMessage(), "A connector should not be marked as dirty while a response is being written")) {
                        // explain exception
                        String message = String.format(
                                "Some datasource listener has modified the component while it is in rendering state. Please refresh datasource '%s' explicitly",
                                datasource.getId());
                        throw new IllegalStateException(message, ex);
                    }

                    throw ex;
                }
            } else {
                CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
            }
        }

        return datasource.getItems();
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        final Item item = getItem(itemId);
        return item == null ? null : item.getItemProperty(propertyId);
    }

    @Override
    public Class getType(Object propertyId) {
        MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;
        return propertyPath.getRangeJavaClass();
    }

    @Override
    public int size() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return datasource.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        //noinspection unchecked
        return itemId != null && datasource.containsItem(((Entity) itemId).getId());
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
    public boolean addContainerProperty(Object propertyId, Class type, Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        //noinspection SuspiciousMethodCalls
        return this.properties.remove(propertyId);
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners == null) {
            itemSetChangeListeners = new LinkedList<>();
        }

        itemSetChangeListeners.add(listener);
    }

    @Override
    public void addListener(ItemSetChangeListener listener) {
        addItemSetChangeListener(listener);
    }

    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners != null) {
            itemSetChangeListeners.remove(listener);

            if (itemSetChangeListeners.isEmpty()) {
                itemSetChangeListeners = null;
            }
        }
    }

    @Override
    public void removeListener(ItemSetChangeListener listener) {
        removeItemSetChangeListener(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object nextItemId(Object itemId) {
        if (datasource instanceof CollectionDatasource.Ordered) {
            Object id = ((CollectionDatasource.Ordered) datasource).nextItemId(((Entity) itemId).getId());
            return datasource.getItem(id);
        }
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object prevItemId(Object itemId) {
        if (datasource instanceof CollectionDatasource.Ordered) {
            Object id = ((CollectionDatasource.Ordered) datasource).prevItemId(((Entity) itemId).getId());
            return datasource.getItem(id);
        }
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object firstItemId() {
        if (datasource instanceof CollectionDatasource.Ordered) {
            Object id = ((CollectionDatasource.Ordered) datasource).firstItemId();
            return datasource.getItem(id);
        }
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object lastItemId() {
        if (datasource instanceof CollectionDatasource.Ordered) {
            Object id = ((CollectionDatasource.Ordered) datasource).lastItemId();
            return datasource.getItem(id);
        }
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isFirstId(Object itemId) {
        if (datasource instanceof CollectionDatasource.Ordered) {
            Object id = ((CollectionDatasource.Ordered) datasource).firstItemId();
            return ObjectUtils.equals(datasource.getItem(id), itemId);
        }
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isLastId(Object itemId) {
        if (datasource instanceof CollectionDatasource.Ordered) {
            Object id = ((CollectionDatasource.Ordered) datasource).lastItemId();
            return ObjectUtils.equals(datasource.getItem(id), itemId);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    @Override
    public String toString() {
        return "{ds=" + (datasource == null ? "null" : datasource.getId() + "}");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void unsubscribe() {
        this.datasource.removeCollectionChangeListener(weakDsListenerAdapter);
        this.datasource.removeItemPropertyChangeListener(weakDsListenerAdapter);
        this.datasource.removeStateChangeListener(weakDsListenerAdapter);

        weakDsListenerAdapter = null;
        datasource = null;
    }

    public void setExecuteAutoRefreshInvalid(boolean executeAutoRefreshInvalid) {
        this.executeAutoRefreshInvalid = executeAutoRefreshInvalid;
    }
}