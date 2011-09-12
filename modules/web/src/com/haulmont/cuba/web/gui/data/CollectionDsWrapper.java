/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 16:17:57
 * $Id$
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class CollectionDsWrapper implements Container, Container.ItemSetChangeNotifier {

    protected boolean autoRefresh;
    protected boolean ignoreListeners;

    protected CollectionDatasource datasource;

    protected Collection<MetaPropertyPath> properties = new ArrayList<MetaPropertyPath>();
    private List<ItemSetChangeListener> itemSetChangeListeners = new ArrayList<ItemSetChangeListener>();

//    private PersistenceManagerService persistenceManager;

    private static Log log = LogFactory.getLog(CollectionDsWrapper.class);

    protected DsManager dsManager;
    
    private static final long serialVersionUID = 1440434590495905389L;

    public CollectionDsWrapper(CollectionDatasource datasource, DsManager dsManager) {
        this(datasource, false, dsManager);
    }

    public CollectionDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties, DsManager dsManager) {
        this(datasource, properties, false, dsManager);
    }

    public CollectionDsWrapper(CollectionDatasource datasource, boolean autoRefresh, DsManager dsManager) {
        this(datasource, null, autoRefresh, dsManager);
    }

    public CollectionDsWrapper(
            CollectionDatasource datasource,
            Collection<MetaPropertyPath> properties,
            boolean autoRefresh,
            DsManager dsManager
    ) {
        this.datasource = datasource;
        this.dsManager = dsManager;
        this.autoRefresh = autoRefresh;
//        this.persistenceManager = ServiceLocator.lookup(PersistenceManagerService.NAME);

        final View view = datasource.getView();
        final MetaClass metaClass = datasource.getMetaClass();

        if (properties == null) {
            createProperties(view, metaClass);
        } else {
            this.properties.addAll(properties);
        }

        dsManager.addListener(createDatasourceListener());
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

    public Item getItem(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        final Object item = datasource.getItem(itemId);
        return item == null ? null : getItemWrapper(item);
    }

    protected Map<Object, ItemWrapper> itemsCache = new HashMap<Object, ItemWrapper>();

    protected synchronized Item getItemWrapper(Object item) {
        ItemWrapper wrapper = itemsCache.get(item);
        if (wrapper == null) {
            wrapper = createItemWrapper(item);
            itemsCache.put(item, wrapper);
        }

        return wrapper;
    }

    protected ItemWrapper createItemWrapper(Object item) {
        return new ItemWrapper(item, properties, dsManager);
    }

    public Collection getContainerPropertyIds() {
        return properties;
    }

    public synchronized Collection getItemIds() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return datasource.getItemIds();
    }

    public Property getContainerProperty(Object itemId, Object propertyId) {
        final Item item = getItem(itemId);
        return item == null ? null : item.getItemProperty(propertyId);
    }

    public Class getType(Object propertyId) {
        MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;
        return propertyPath.getRangeJavaClass();
    }

    public synchronized int size() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return datasource.size();
    }

    public synchronized boolean containsId(Object itemId) {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return datasource.containsItem(itemId);
    }

    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean addContainerProperty(Object propertyId, Class type, Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        return this.properties.remove(propertyId);
    }

    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void addListener(ItemSetChangeListener listener) {
        this.itemSetChangeListeners.add(listener);
    }

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
        public void itemChanged(Datasource ds, Entity prevItem, Entity item) {}

        public void stateChanged(Datasource<Entity> ds, Datasource.State prevState, Datasource.State state) {
            final boolean prevIgnoreListeners = ignoreListeners;
            try {
                itemsCache.clear();
                fireItemSetChanged();
            } finally {
                ignoreListeners = prevIgnoreListeners;
            }
        }

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

        public void collectionChanged(CollectionDatasource ds, Operation operation) {
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
            implements LazyCollectionDatasourceListener<Entity>
    {
        public void completelyLoaded(CollectionDatasource.Lazy ds) {
            checkMaxFetchUI(ds);
        }
    }

    @Override
    public String toString() {
        return "{ds=" + (datasource == null ? "null" : datasource.getId() + "}");
    }
}
