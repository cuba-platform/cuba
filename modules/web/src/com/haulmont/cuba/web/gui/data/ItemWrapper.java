/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 17:01:30
 * $Id$
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import java.util.*;

public class ItemWrapper implements Item, Item.PropertySetChangeNotifier {

    private Map<MetaPropertyPath, PropertyWrapper> properties = new HashMap<MetaPropertyPath, PropertyWrapper>();
    private List<PropertySetChangeListener> listeners = new ArrayList<PropertySetChangeListener>();

    protected Object item;

    private static final long serialVersionUID = -7298696379571470141L;

    public ItemWrapper(Object item, MetaClass metaClass, DsManager dsManager) {
        this(item, MetadataHelper.getPropertyPaths(metaClass), dsManager);
    }

    public ItemWrapper(Object item, Collection<MetaPropertyPath> properties, DsManager dsManager) {
        this.item = item;

        for (MetaPropertyPath property : properties) {
            this.properties.put(property, createPropertyWrapper(item, property, dsManager));
        }

        if (item instanceof CollectionDatasource) {
            dsManager.addListener(new CollectionDatasourceListener<Entity>() {
                public void collectionChanged(CollectionDatasource ds, Operation operation) {}
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

    protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath, DsManager dsManager) {
        return new PropertyWrapper(item, propertyPath, dsManager);
    }

    public Property getItemProperty(Object id) {
        if (id instanceof MetaPropertyPath) {
            return properties.get(id);
        } else if (id instanceof MetaProperty) {
            final MetaProperty metaProperty = (MetaProperty) id;
            return properties.get(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
        } else {
            throw new UnsupportedOperationException("Unsupported item property: " + id);
        }
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
        return entity == null ? "" : entity.getInstanceName();
    }

    public Entity getItem() {
        return item instanceof Datasource ? ((Datasource) item).getItem() : (Entity) item;
    }
}
