/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class CollectionDsWrapper implements Container, Container.ItemSetChangeNotifier {

    private static final long serialVersionUID = 1440434590495905389L;

    protected boolean autoRefresh;
    protected boolean ignoreListeners;

    protected CollectionDatasource datasource;

    protected Collection<MetaPropertyPath> properties = new ArrayList<>();
    private List<ItemSetChangeListener> itemSetChangeListeners = new ArrayList<>();

    public CollectionDsWrapper(CollectionDatasource datasource) {
        this(datasource, false);
    }

    public CollectionDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties) {
        this(datasource, properties, false);
    }

    public CollectionDsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
        this(datasource, null, autoRefresh);
    }

    public CollectionDsWrapper(
            CollectionDatasource datasource,
            Collection<MetaPropertyPath> properties,
            boolean autoRefresh
    ) {
        this.datasource = datasource;
        this.autoRefresh = autoRefresh;

        final View view = datasource.getView();
        final MetaClass metaClass = datasource.getMetaClass();

        if (properties == null) {
            createProperties(view, metaClass);
        } else {
            this.properties.addAll(properties);
        }

        datasource.addListener(createDatasourceListener());
    }

    protected DatasourceListener createDatasourceListener() {
        if (datasource instanceof CollectionDatasource.Lazy)
            return new LazyDataSourceRefreshListener();
        else
            return new DataSourceRefreshListener();
    }

    protected void createProperties(View view, MetaClass metaClass) {
        properties.addAll(CollectionDsHelper.createProperties(view, metaClass));
    }

    protected void fireItemSetChanged() {
        if (ignoreListeners) return;
        ignoreListeners = true;

        for (ItemSetChangeListener listener : itemSetChangeListeners) {
            listener.containerItemSetChange(new ItemSetChangeEvent() {
                public Container getContainer() {
                    return CollectionDsWrapper.this;
                }
            });
        }
    }

    @Override
    public Item getItem(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        final Object item = datasource.getItem(itemId);
        return item == null ? null : getItemWrapper(item);
    }

    protected Map<Object, ItemWrapper> itemsCache = new HashMap<>();

    protected synchronized Item getItemWrapper(Object item) {
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
    public void addListener(ItemSetChangeListener listener) {
        this.itemSetChangeListeners.add(listener);
    }

    @Override
    public void removeListener(ItemSetChangeListener listener) {
        this.itemSetChangeListeners.remove(listener);
    }

    protected void checkMaxFetchUI(CollectionDatasource ds) {
//        String entityName = ds.getMetaClass().getName();
//        if (ds.size() >= persistenceManager.getMaxFetchUI(entityName)) {
//            log.debug("MaxFetchUI threshold exceeded for " + entityName);
//            String msg = MessageProvider.getMessage(AppConfig.getMessagesPack(), "maxFetchUIExceeded");
//            App app = App.getInstance();
//            app.getAppLog().debug(entityName + ": " + msg);
//            app.getWindowManager().showNotification(msg, IFrame.NotificationType.HUMANIZED);
//        }
    }

    protected class DataSourceRefreshListener implements CollectionDatasourceListener<Entity> {
        @Override
        public void itemChanged(Datasource ds, Entity prevItem, Entity item) {}

        @Override
        public void stateChanged(Datasource<Entity> ds, Datasource.State prevState, Datasource.State state) {
            final boolean prevIgnoreListeners = ignoreListeners;
            try {
                itemsCache.clear();
            } finally {
                ignoreListeners = prevIgnoreListeners;
            }
        }

        @Override
        public void valueChanged(Entity source, String property, Object prevValue, Object value) {
            Item wrapper = getItemWrapper(source);

            // MetaProperty worked wrong with properties from inherited superclasses
            MetaPropertyPath metaPropertyPath = datasource.getMetaClass().getPropertyPath(property);
            if (metaPropertyPath == null) {
                return;
            }
            Property itemProperty = wrapper.getItemProperty(metaPropertyPath);
            if (itemProperty instanceof PropertyWrapper) {
                ((PropertyWrapper) itemProperty).fireValueChangeEvent();
            }
        }

        @Override
        public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
            final boolean prevIgnoreListeners = ignoreListeners;
            try {
                itemsCache.clear();
                fireItemSetChanged();
                if (!(ds instanceof CollectionDatasource.Lazy)) {
                    checkMaxFetchUI(ds);
                }
            } finally {
                ignoreListeners = prevIgnoreListeners;
            }
        }
    }

    protected class LazyDataSourceRefreshListener extends DataSourceRefreshListener
            implements LazyCollectionDatasourceListener<Entity> {
        @Override
        public void completelyLoaded(CollectionDatasource.Lazy ds) {
            checkMaxFetchUI(ds);
        }
    }

    @Override
    public String toString() {
        return "{ds=" + (datasource == null ? "null" : datasource.getId() + "}");
    }
}