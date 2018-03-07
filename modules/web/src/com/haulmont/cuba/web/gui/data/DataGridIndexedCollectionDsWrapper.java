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
 */

package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.haulmont.cuba.web.gui.components.WebDataGrid.CollectionDsListenersWrapper;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.AbstractContainer;
import com.vaadin.ui.UI;

import java.util.*;

public class DataGridIndexedCollectionDsWrapper
        extends
            AbstractContainer
        implements
            Container.Indexed, Container.PropertySetChangeNotifier, Container.ItemSetChangeNotifier,
            UnsubscribableDsWrapper {

    protected boolean autoRefresh;
    protected boolean ignoreListeners;

    protected CollectionDatasource.Indexed datasource;
    protected Collection<MetaPropertyPath> properties = new ArrayList<>();
    protected CollectionDsListenersWrapper collectionDsListenersWrapper;

    protected Datasource.StateChangeListener cdsStateChangeListener;
    protected Datasource.ItemPropertyChangeListener cdsItemPropertyChangeListener;
    protected CollectionDatasource.CollectionChangeListener cdsCollectionChangeListener;

    public DataGridIndexedCollectionDsWrapper(CollectionDatasource datasource, boolean autoRefresh,
                                              CollectionDsListenersWrapper collectionDsListenersWrapper) {
        this(datasource, null, autoRefresh, collectionDsListenersWrapper);
    }

    @SuppressWarnings("unchecked")
    public DataGridIndexedCollectionDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties,
                                              boolean autoRefresh,
                                              CollectionDsListenersWrapper collectionDsListenersWrapper) {
        if (!(datasource instanceof CollectionDatasource.Indexed)) {
            throw new IllegalArgumentException("Datasource must implement " +
                    "com.haulmont.cuba.gui.data.CollectionDatasource.Indexed");
        }
        this.datasource = (CollectionDatasource.Indexed) datasource;
        this.autoRefresh = autoRefresh;
        this.collectionDsListenersWrapper = collectionDsListenersWrapper;

        final View view = datasource.getView();
        final MetaClass metaClass = datasource.getMetaClass();

        if (properties == null) {
            createProperties(view, metaClass);
        } else {
            this.properties.addAll(properties);
        }

        cdsItemPropertyChangeListener = createItemPropertyChangeListener();
        cdsStateChangeListener = createStateChangeListener();
        cdsCollectionChangeListener = createCollectionChangeListener();

        collectionDsListenersWrapper.addItemPropertyChangeListener(cdsItemPropertyChangeListener);
        collectionDsListenersWrapper.addStateChangeListener(cdsStateChangeListener);
        collectionDsListenersWrapper.addCollectionChangeListener(cdsCollectionChangeListener);
    }

    protected void createProperties(View view, MetaClass metaClass) {
        properties.addAll(CollectionDsHelper.createProperties(view, metaClass));
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

    @Override
    public int indexOfId(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        //noinspection unchecked
        return datasource.indexOfId(itemId);
    }

    @Override
    public Object getIdByIndex(int index) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return datasource.getIdByIndex(index);
    }

    @Override
    public List getItemIds(int startIndex, int numberOfItems) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return datasource.getItemIds(startIndex, numberOfItems);
    }

    @Override
    public Object addItemAt(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object nextItemId(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        //noinspection unchecked
        return datasource.nextItemId(itemId);
    }

    @Override
    public Object prevItemId(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        //noinspection unchecked
        return datasource.prevItemId(itemId);
    }

    @Override
    public Object firstItemId() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        //noinspection unchecked
        return datasource.firstItemId();
    }

    @Override
    public Object lastItemId() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        //noinspection unchecked
        return datasource.lastItemId();
    }

    @Override
    public boolean isFirstId(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        //noinspection unchecked
        return datasource.isFirstId(itemId);
    }

    @Override
    public boolean isLastId(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        //noinspection unchecked
        return datasource.isLastId(itemId);
    }

    @Override
    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
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
        return itemsCache.computeIfAbsent(item, k -> createItemWrapper(item));
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
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
            throws UnsupportedOperationException {
        if (propertyId instanceof MetaPropertyPath) {
            if (this.properties.add((MetaPropertyPath) propertyId)) {
                fireContainerPropertySetChange();
                return true;
            }
            return false;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        //noinspection SuspiciousMethodCalls
        if (this.properties.remove(propertyId)) {
            fireContainerPropertySetChange();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPropertySetChangeListener(PropertySetChangeListener listener) {
        super.addPropertySetChangeListener(listener);
    }

    @Override
    public void addListener(PropertySetChangeListener listener) {
        super.addListener(listener);
    }

    @Override
    public void removePropertySetChangeListener(PropertySetChangeListener listener) {
        super.removePropertySetChangeListener(listener);
    }

    @Override
    public void removeListener(PropertySetChangeListener listener) {
        super.removeListener(listener);
    }

    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        super.addItemSetChangeListener(listener);
    }

    @Override
    public void addListener(ItemSetChangeListener listener) {
        super.addListener(listener);
    }

    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {
        super.removeItemSetChangeListener(listener);
    }

    @Override
    public void removeListener(ItemSetChangeListener listener) {
        super.removeListener(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void unsubscribe() {
        collectionDsListenersWrapper.removeCollectionChangeListener(cdsCollectionChangeListener);
        collectionDsListenersWrapper.removeItemPropertyChangeListener(cdsItemPropertyChangeListener);
        collectionDsListenersWrapper.removeStateChangeListener(cdsStateChangeListener);

        datasource = null;
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

        if (getItemSetChangeListeners() != null) {
            StaticItemSetChangeEvent event = new StaticItemSetChangeEvent(this);
            for (ItemSetChangeListener listener : getItemSetChangeListeners()) {
                listener.containerItemSetChange(event);
            }
        }
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
            MetaClass metaClass = datasource.getMetaClass();
            String property = e.getProperty();
            MetaPropertyPath metaPropertyPath = metaClass.getPropertyPath(property);
            if (metaPropertyPath == null && DynamicAttributesUtils.isDynamicAttribute(property)) {
                metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(metaClass, property);
            }
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