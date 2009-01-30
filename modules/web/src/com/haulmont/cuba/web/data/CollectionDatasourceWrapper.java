/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 16:17:57
 * $Id$
 */
package com.haulmont.cuba.web.data;

import com.itmill.toolkit.data.Container;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;

import java.util.*;

public class CollectionDatasourceWrapper implements Container, Container.ItemSetChangeNotifier {

    protected CollectionDatasource datasource;
    private Collection<MetaProperty> properties = new ArrayList<MetaProperty>();

    private List<ItemSetChangeListener> itemSetChangeListeners = new ArrayList<ItemSetChangeListener>();

    public CollectionDatasourceWrapper(CollectionDatasource datasource) {
        this.datasource = datasource;
        final View view = datasource.getView();
        final MetaClass metaClass = datasource.getMetaClass();

        if (view != null) {
            for (ViewProperty property : view.getProperties()) {
                final String name = property.getName();

                final MetaProperty metaProperty = metaClass.getProperty(name);
                final Range range = metaProperty.getRange();

                final Range.Cardinality cardinality = range.getCardinality();
                if (Range.Cardinality.ONE_TO_ONE.equals(cardinality) ||
                        Range.Cardinality.MANY_TO_ONE.equals(cardinality))
                {
                    properties.add(metaProperty);
                }
            }
        } else {
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                final Range range = metaProperty.getRange();

                final Range.Cardinality cardinality = range.getCardinality();
                if (Range.Cardinality.ONE_TO_ONE.equals(cardinality) ||
                        Range.Cardinality.MANY_TO_ONE.equals(cardinality))
                {
                    properties.add(metaProperty);
                }
            }
        }

        datasource.addListener(new CollectionDatasourceListener() {
            public void itemChanged(Datasource ds, Object prevItem, Object item) {}

            public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
                fireItemSetChanged();
            }

            public void valueChanged(Object source, String property, Object prevValue, Object value) {}

            public void collectionChanged(Datasource ds, CollectionOperation operation) {
                fireItemSetChanged();
            }
        });
    }

    protected void fireItemSetChanged() {
        for (ItemSetChangeListener listener : itemSetChangeListeners) {
            listener.containerItemSetChange(new ItemSetChangeEvent() {
                public Container getContainer() {
                    return CollectionDatasourceWrapper.this;
                }
            });
        }
    }

    public Item getItem(Object itemId) {
        final Object item = datasource.getItem(itemId);
        return item == null ? null : getItemWrapper(item);
    }

    protected Map<Object, ItemWrapper> itemsCache = new HashMap<Object, ItemWrapper>();

    protected synchronized Item getItemWrapper(Object item) {
        ItemWrapper wrapper = itemsCache.get(item);
        if (wrapper == null) {
            wrapper = new ItemWrapper(item, properties);
            itemsCache.put(item, wrapper);
        }

        return wrapper;
    }

    public Collection getContainerPropertyIds() {
        return properties;
    }

    public Collection getItemIds() {
        return datasource.getItemIds();
    }

    public Property getContainerProperty(Object itemId, Object propertyId) {
        final Item item = getItem(itemId);
        return item == null ? null : item.getItemProperty(propertyId);
    }

    public Class getType(Object propertyId) {
        MetaProperty metaProperty = (MetaProperty) propertyId;
        return MetadataHelper.getPropertyTypeClass(metaProperty);
    }

    public int size() {
        return datasource.size();
    }

    public boolean containsId(Object itemId) {
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
}
