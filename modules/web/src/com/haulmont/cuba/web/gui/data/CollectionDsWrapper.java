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
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakStateChangeListener;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;

import java.util.*;

public class CollectionDsWrapper implements Container, Container.ItemSetChangeNotifier {

    protected boolean autoRefresh;
    protected boolean ignoreListeners;

    protected CollectionDatasource datasource;
    protected Collection<MetaPropertyPath> properties = new ArrayList<>();

    // lazily initialized listeners list
    protected List<ItemSetChangeListener> itemSetChangeListeners = null;

    protected Datasource.StateChangeListener cdsStateChangeListener;
    protected Datasource.ItemPropertyChangeListener cdsItemPropertyChangeListener;
    protected CollectionDatasource.CollectionChangeListener cdsCollectionChangeListener;

    public CollectionDsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
        this(datasource, null, autoRefresh);
    }

    public CollectionDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties,
                               boolean autoRefresh) {
        this.datasource = datasource;
        this.autoRefresh = autoRefresh;

        final View view = datasource.getView();
        final MetaClass metaClass = datasource.getMetaClass();

        if (properties == null) {
            createProperties(view, metaClass);
        } else {
            this.properties.addAll(properties);
        }

        cdsStateChangeListener = createStateChangeListener();
        //noinspection unchecked
        datasource.addStateChangeListener(new WeakStateChangeListener(datasource, cdsStateChangeListener));

        cdsItemPropertyChangeListener = createItemPropertyChangeListener();
        //noinspection unchecked
        datasource.addItemPropertyChangeListener(new WeakItemPropertyChangeListener(datasource, cdsItemPropertyChangeListener));

        cdsCollectionChangeListener = createCollectionChangeListener();
        //noinspection unchecked
        datasource.addCollectionChangeListener(new WeakCollectionChangeListener(datasource, cdsCollectionChangeListener));
    }

    protected CollectionDatasource.CollectionChangeListener createCollectionChangeListener() {
        return new ContainerDatasourceCollectionChangeListener();
    }

    protected Datasource.ItemPropertyChangeListener createItemPropertyChangeListener() {
        return new ContainerDatasourceItemPropertyChangeListener();
    }

    protected Datasource.StateChangeListener createStateChangeListener() {
        return new ContainerDatasourceStateChangeListener();
    }

    protected void createProperties(View view, MetaClass metaClass) {
        properties.addAll(CollectionDsHelper.createProperties(view, metaClass));
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

        if (itemSetChangeListeners != null) {
            StaticItemSetChangeEvent event = new StaticItemSetChangeEvent(this);
            for (ItemSetChangeListener listener : itemSetChangeListeners) {
                listener.containerItemSetChange(event);
            }
        }
    }

    @Override
    public Item getItem(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        //noinspection unchecked
        final Object item = datasource.getItem(itemId);
        return item == null ? null : getItemWrapper(item);
    }

    protected Map<Object, ItemWrapper> itemsCache = new HashMap<>();

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
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return datasource.getItemIds();
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
        return datasource.containsItem(itemId);
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
        if (propertyId instanceof MetaPropertyPath) {
            return this.properties.add((MetaPropertyPath) propertyId);
        }
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

    protected class ContainerDatasourceStateChangeListener implements Datasource.StateChangeListener {

        public ContainerDatasourceStateChangeListener() {
        }

        @Override
        public void stateChanged(Datasource.StateChangeEvent e) {
            itemsCache.clear();
        }
    }

    protected class ContainerDatasourceCollectionChangeListener implements CollectionDatasource.CollectionChangeListener {

        public ContainerDatasourceCollectionChangeListener() {
        }

        @Override
        public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
            itemsCache.clear();

            final boolean prevIgnoreListeners = ignoreListeners;
            try {
                fireItemSetChanged();
            } finally {
                ignoreListeners = prevIgnoreListeners;
            }
        }
    }

    protected class ContainerDatasourceItemPropertyChangeListener implements Datasource.ItemPropertyChangeListener {

        public ContainerDatasourceItemPropertyChangeListener() {
        }

        @Override
        public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
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
        }
    }

    @Override
    public String toString() {
        return "{ds=" + (datasource == null ? "null" : datasource.getId() + "}");
    }
}