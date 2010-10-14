/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 16:17:57
 * $Id$
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.web.App;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.MetaPropertyPath;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class CollectionDsWrapper implements Container, Container.ItemSetChangeNotifier {

    protected boolean autoRefresh;
    protected boolean ignoreListeners;

    protected CollectionDatasource datasource;

    protected Collection<MetaPropertyPath> properties = new ArrayList<MetaPropertyPath>();
    private List<ItemSetChangeListener> itemSetChangeListeners = new ArrayList<ItemSetChangeListener>();

    private PersistenceManagerService persistenceManager;

    private static Log log = LogFactory.getLog(CollectionDsWrapper.class);

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
                    boolean autoRefresh)
    {
        this.datasource = datasource;
        this.autoRefresh = autoRefresh;
        this.persistenceManager = ServiceLocator.lookup(PersistenceManagerService.NAME);

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
        if (view != null) {
            for (ViewProperty property : view.getProperties()) {
                final String name = property.getName();

                final MetaProperty metaProperty = metaClass.getProperty(name);
                final Range range = metaProperty.getRange();
                if (range == null) continue;

                final Range.Cardinality cardinality = range.getCardinality();
                if (Range.Cardinality.ONE_TO_ONE.equals(cardinality) ||
                        Range.Cardinality.MANY_TO_ONE.equals(cardinality))
                {
                    properties.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
                }
            }
        } else {
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                final Range range = metaProperty.getRange();
                if (range == null) continue;

                final Range.Cardinality cardinality = range.getCardinality();
                if (Range.Cardinality.ONE_TO_ONE.equals(cardinality) ||
                        Range.Cardinality.MANY_TO_ONE.equals(cardinality))
                {
                    properties.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
                }
            }
        }
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
        __autoRefreshInvalid();
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
        return new ItemWrapper(item, properties);
    }

    public Collection getContainerPropertyIds() {
        return properties;
    }

    public synchronized Collection getItemIds() {
        __autoRefreshInvalid();
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
        __autoRefreshInvalid();
        return datasource.size();
    }

    public synchronized boolean containsId(Object itemId) {
        __autoRefreshInvalid();
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

    protected void __autoRefreshInvalid() {
        if (autoRefresh && Datasource.State.INVALID.equals(datasource.getState())) {
            DsContext dsContext = datasource.getDsContext();
            Map<String, Object> params = null;
            if (dsContext != null && dsContext.getWindowContext() != null) {
                params = dsContext.getWindowContext().getParams();
            }
            if (params == null || !BooleanUtils.isTrue((Boolean) params.get("disableAutoRefresh"))) {
                if (datasource instanceof CollectionDatasource.Suspendable)
                    ((CollectionDatasource.Suspendable) datasource).refreshIfNotSuspended();
                else
                    datasource.refresh();
            }
        }
    }

    protected void checkMaxFetchUI(CollectionDatasource ds) {
        String entityName = ds.getMetaClass().getName();
        if (ds.size() >= persistenceManager.getMaxFetchUI(entityName)) {
            log.debug("MaxFetchUI threshold exceeded for " + entityName);
            String msg = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "maxFetchUIExceeded");
            App app = App.getInstance();
            app.getAppLog().debug(entityName + ": " + msg);
            app.getWindowManager().showNotification(msg, IFrame.NotificationType.HUMANIZED);
        }
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

            MetaProperty metaProperty = datasource.getMetaClass().getProperty(property);
            if (metaProperty == null) {
                return;
            }
            Property itemProperty = wrapper.getItemProperty(metaProperty);
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
