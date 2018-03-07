/*
 * Copyright (c) 2008-2017 Haulmont.
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
package com.haulmont.cuba.web.gui.model;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.impl.WeakContainerListenerAdapter;
import com.haulmont.cuba.web.gui.data.StaticItemSetChangeEvent;
import com.haulmont.cuba.web.gui.data.UnsubscribableDsWrapper;
import com.vaadin.ui.UI;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionContainerAdapter implements Container, Container.ItemSetChangeNotifier, UnsubscribableDsWrapper {

    protected boolean autoRefresh;
    protected boolean ignoreListeners;

    protected CollectionContainer<Entity> collectionContainer;
    protected Collection<MetaPropertyPath> properties = new ArrayList<>();

    // lazily initialized listeners list
    protected List<Container.ItemSetChangeListener> itemSetChangeListeners = null;

//    protected EntityContainer.StateChangeListener cdsStateChangeListener;
    protected InstanceContainer.ItemPropertyChangeListener cdsItemPropertyChangeListener;
    protected CollectionContainer.CollectionChangeListener cdsCollectionChangeListener;

    protected WeakContainerListenerAdapter weakDsListenerAdapter;

    public CollectionContainerAdapter(CollectionContainer collectionContainer, boolean autoRefresh) {
        this(collectionContainer, null, autoRefresh);
    }

    @SuppressWarnings("unchecked")
    public CollectionContainerAdapter(CollectionContainer collectionContainer, Collection<MetaPropertyPath> properties,
                                      boolean autoRefresh) {
        this.collectionContainer = collectionContainer;
        this.autoRefresh = autoRefresh;

//        final View view = collectionContainer.getView();
        final MetaClass metaClass = collectionContainer.getMetaClass();

//        if (properties == null) {
//            createProperties(view, metaClass);
//        } else {
            this.properties.addAll(properties);
//        }

        cdsItemPropertyChangeListener = createItemPropertyChangeListener();
//        cdsStateChangeListener = createStateChangeListener();
        cdsCollectionChangeListener = createCollectionChangeListener();

        weakDsListenerAdapter = new WeakContainerListenerAdapter(collectionContainer, cdsItemPropertyChangeListener,
                /*cdsStateChangeListener,*/ cdsCollectionChangeListener);

        collectionContainer.addItemPropertyChangeListener(weakDsListenerAdapter);
//        collectionContainer.addStateChangeListener(weakDsListenerAdapter);
        collectionContainer.addCollectionChangeListener(weakDsListenerAdapter);
    }

    protected CollectionContainer.CollectionChangeListener createCollectionChangeListener() {
        return new ContainerDatasourceCollectionChangeListener();
    }

    protected InstanceContainer.ItemPropertyChangeListener createItemPropertyChangeListener() {
        return new ContainerDatasourceItemPropertyChangeListener();
    }

//    protected EntityContainer.StateChangeListener createStateChangeListener() {
//        return new ContainerDatasourceStateChangeListener();
//    }

//    protected void createProperties(View view, MetaClass metaClass) {
//        properties.addAll(CollectionDsHelper.createProperties(view, metaClass));
//    }

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
//        CollectionDsHelper.autoRefreshInvalid(collectionContainer, autoRefresh);
        //noinspection unchecked
        final Object item = collectionContainer.getItem(itemId);
        return item == null ? null : getItemAdapter(item);
    }

    protected Map<Object, ItemAdapter> itemsCache = new HashMap<>();

    protected Item getItemAdapter(Object item) {
        ItemAdapter wrapper = itemsCache.get(item);
        if (wrapper == null) {
            wrapper = createItemAdapter(item);
            itemsCache.put(item, wrapper);
        }

        return wrapper;
    }

    protected ItemAdapter createItemAdapter(Object item) {
        return new ItemAdapter(item, collectionContainer.getMetaClass(), properties);
    }

    @Override
    public Collection getContainerPropertyIds() {
        return properties;
    }

    @Override
    public Collection getItemIds() {
//        CollectionDsHelper.autoRefreshInvalid(collectionContainer, autoRefresh);
        return collectionContainer.getItems().stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
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
//        CollectionDsHelper.autoRefreshInvalid(collectionContainer, autoRefresh);
        return collectionContainer.getItems().size();
    }

    @Override
    public boolean containsId(Object itemId) {
//        CollectionDsHelper.autoRefreshInvalid(collectionContainer, autoRefresh);
        //noinspection unchecked
        return collectionContainer.getItem(itemId) != null;
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

    @SuppressWarnings("unchecked")
    @Override
    public void unsubscribe() {
        collectionContainer.removeCollectionChangeListener(weakDsListenerAdapter);
        collectionContainer.removeItemPropertyChangeListener(weakDsListenerAdapter);
//        collectionContainer.removeStateChangeListener(weakDsListenerAdapter);

        weakDsListenerAdapter = null;
        collectionContainer = null;
    }

//    protected class ContainerDatasourceStateChangeListener implements EntityContainer.StateChangeListener {
//
//        public ContainerDatasourceStateChangeListener() {
//        }
//
//        @Override
//        public void stateChanged(EntityContainer.StateChangeEvent e) {
//            itemsCache.clear();
//        }
//    }

    protected class ContainerDatasourceCollectionChangeListener implements CollectionContainer.CollectionChangeListener {

        public ContainerDatasourceCollectionChangeListener() {
        }

        @Override
        public void collectionChanged(CollectionContainer.CollectionChangeEvent e) {
            itemsCache.clear();

            final boolean prevIgnoreListeners = ignoreListeners;
            try {
                fireItemSetChanged();
            } finally {
                ignoreListeners = prevIgnoreListeners;
            }
        }
    }

    protected class ContainerDatasourceItemPropertyChangeListener implements InstanceContainer.ItemPropertyChangeListener {

        public ContainerDatasourceItemPropertyChangeListener() {
        }

        @Override
        public void itemPropertyChanged(InstanceContainer.ItemPropertyChangeEvent e) {
            Item wrapper = getItemAdapter(e.getItem());

            // MetaProperty worked wrong with properties from inherited superclasses
            MetaPropertyPath metaPropertyPath = collectionContainer.getMetaClass().getPropertyPath(e.getProperty());
            if (metaPropertyPath == null) {
                return;
            }
            Property itemProperty = wrapper.getItemProperty(metaPropertyPath);
            if (itemProperty instanceof PropertyAdapter) {
                ((PropertyAdapter) itemProperty).fireValueChangeEvent();
            }
        }
    }

    @Override
    public String toString() {
        return "{ds=" + (collectionContainer == null ? "null" : collectionContainer.toString() + "}");
    }
}