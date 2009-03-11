/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 17:01:30
 * $Id$
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.core.entity.Entity;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;

import java.util.*;

public class ItemWrapper implements Item, Item.PropertySetChangeNotifier {
    private Map<MetaProperty, PropertyWrapper> properties = new HashMap<MetaProperty, PropertyWrapper>();
    private List<PropertySetChangeListener> listeners = new ArrayList<PropertySetChangeListener>();

    protected Object item;

    public ItemWrapper(Object item, Collection<MetaProperty> properties) {
        this.item = item;

        for (MetaProperty property : properties) {
            this.properties.put(property, createPropertyWrapper(item, property));
        }

        if (item instanceof CollectionDatasource) {
            ((CollectionDatasource) item).addListener(new CollectionDatasourceListener<Entity>() {
                public void collectionChanged(Datasource<Entity> ds, CollectionOperation operation) {}
                public void itemChanged(Datasource<Entity> ds, Entity prevItem, Entity item) {
                    fireItemProperySetChanged();
                }
                public void stateChanged(Datasource<Entity> ds, Datasource.State prevState, Datasource.State state) {}
                public void valueChanged(Entity source, String property, Object prevValue, Object value) {}
            });
        }
    }

    protected void fireItemProperySetChanged() {
        for (PropertySetChangeListener listener : listeners) {
            listener.itemPropertySetChange(new PropertySetChangeEvent());
        }
    }

    protected PropertyWrapper createPropertyWrapper(Object item, MetaProperty property) {
        return new PropertyWrapper(item, property);
    }

    public Property getItemProperty(Object id) {
        return properties.get(id);
    }

    public Collection getItemPropertyIds() {
        return properties.keySet();
    }

    public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void addListener(PropertySetChangeListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(PropertySetChangeListener listener) {
        listeners.remove(listener);
    }

    private class PropertySetChangeEvent implements Item.PropertySetChangeEvent {
        public Item getItem() {
            return ItemWrapper.this;
        }
    }

    @Override
    public String toString() {
        final Entity entity = getItem();
        return entity == null ? "" : entity.toString();
    }

    public Entity getItem() {
        return item instanceof Datasource ? ((Datasource) item).getItem() : (Entity) item;
    }
}
