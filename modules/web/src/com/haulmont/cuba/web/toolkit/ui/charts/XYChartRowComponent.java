/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.charts;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractComponent;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public class XYChartRowComponent  extends AbstractComponent implements XYChartRow {

    private static final long serialVersionUID = 1527647312094602957L;

    protected Container items;
    protected Object xPropertyId, yPropertyId;

    protected Set<ItemSetChangeListener> itemSetChangeListeners = new LinkedHashSet<ItemSetChangeListener>();
    protected Set<PropertySetChangeListener> propertySetChangeListeners = new LinkedHashSet<PropertySetChangeListener>();

    public Item getItem(Object itemId) {
        return items.getItem(itemId);
    }

    public Collection<?> getContainerPropertyIds() {
        return items.getContainerPropertyIds();
    }

    public Collection<?> getItemIds() {
        return items.getItemIds();
    }

    public Property getContainerProperty(Object itemId, Object propertyId) {
        return items.getContainerProperty(itemId, propertyId);
    }

    public Class<?> getType(Object propertyId) {
        return items.getType(propertyId);
    }

    public int size() {
        return items.size();
    }

    public boolean containsId(Object itemId) {
        return items.containsId(itemId);
    }

    public Item addItem(Object itemId) throws UnsupportedOperationException {
        final Item retval = items.addItem(itemId);
        if (retval != null && !(items instanceof ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    public Object addItem() throws UnsupportedOperationException {
        final Object retval = items.addItem();
        if (retval != null && !(items instanceof ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        final boolean retval = items.removeItem(itemId);
        if (retval && !(items instanceof ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        final boolean retval = items.addContainerProperty(propertyId, type, defaultValue);
        if (retval && !(items instanceof PropertySetChangeNotifier)) {
            firePropertySetChange();
        }
        return retval;
    }

    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        final boolean retval = items.removeContainerProperty(propertyId);
        if (retval && !(items instanceof PropertySetChangeNotifier)) {
            firePropertySetChange();
        }
        return retval;
    }

    public boolean removeAllItems() throws UnsupportedOperationException {
        final boolean retval = items.removeAllItems();
        if (retval && !(items instanceof ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    public Collection<?> getPointIds() {
        return items.getItemIds();
    }

    public void addPoint(Object id) {
        addItem(id);
    }

    public Collection<?> getPropertyIds() {
        return getContainerPropertyIds();
    }

    public void addProperty(Object id) {
        addContainerProperty(id, Object.class, null);
    }

    public Object getXPropertyId() {
        return xPropertyId;
    }

    public void setXPropertyId(Object propertyId) {
        xPropertyId = propertyId;
    }

    public Object getYPropertyId() {
        return yPropertyId;
    }

    public void setYPropertyId(Object propertyId) {
        yPropertyId = propertyId;
    }

    public Object getXValue(Object pointId) {
        Property p = getContainerProperty(pointId, xPropertyId);
        if (p == null) {
            return null;
        }

        Object o = p.getValue();
        return o;
    }

    public void setXValue(Object pointId, Object value) {
        Property p = getContainerProperty(pointId, xPropertyId);
        if (p != null) {
            p.setValue(value);
        }
    }

    public Object getYValue(Object pointId) {
        Property p = getContainerProperty(pointId, yPropertyId);
        if (p == null) {
            return null;
        }

        Object o = p.getValue();
        return o;
    }

    public void setYValue(Object pointId, Object value) {
        Property p = getContainerProperty(pointId, yPropertyId);
        if (p != null) {
            p.setValue(value);
        }
    }

    public void containerItemSetChange(Container.ItemSetChangeEvent event) {
        fireItemSetChange();
    }

    public void addListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners == null) {
            itemSetChangeListeners = new LinkedHashSet<ItemSetChangeListener>();
        }
        itemSetChangeListeners.add(listener);
    }

    public void removeListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners != null) {
            itemSetChangeListeners.remove(listener);
        }
    }

    public void containerPropertySetChange(Container.PropertySetChangeEvent event) {
        firePropertySetChange();
    }

    public void addListener(PropertySetChangeListener listener) {
        propertySetChangeListeners.add(listener);
    }

    public void removeListener(PropertySetChangeListener listener) {
        propertySetChangeListeners.remove(listener);
    }

    public void setContainerDataSource(Container datasource) {
        if (items != datasource) {
            if (items != null) {
                if (items instanceof ItemSetChangeNotifier) {
                    ((ItemSetChangeNotifier) items).removeListener(this);
                }
                if (items instanceof PropertySetChangeNotifier) {
                    ((PropertySetChangeNotifier) items).removeListener(this);
                }
            }

            items = datasource;

            //final Collection<?> propertyIds = items.getContainerPropertyIds();
            //for (final Object id : propertyIds) {
            //    addContainerProperty(id, Object.class, null);
            //}

            if (items instanceof ItemSetChangeNotifier) {
                ((ItemSetChangeNotifier) items).addListener(this);
            }
            if (items instanceof PropertySetChangeNotifier) {
                ((PropertySetChangeNotifier) items).addListener(this);
            }

            requestRepaint();
        }
    }

    public Container getContainerDataSource() {
        return items;
    }

    private void fireItemSetChange() {
        for (final ItemSetChangeListener listener : itemSetChangeListeners) {
            listener.containerItemSetChange(new ItemSetChangeEvent());
        }
        requestRepaint();
    }

    private void firePropertySetChange() {
        for (final PropertySetChangeListener listener : propertySetChangeListeners) {
            listener.containerPropertySetChange(new PropertySetChangeEvent());
        }
        requestRepaint();
    }

    private class ItemSetChangeEvent implements Container.ItemSetChangeEvent {
        private static final long serialVersionUID = -9033778466643564832L;

        public Container getContainer() {
            return XYChartRowComponent.this;
        }
    }

    private class PropertySetChangeEvent implements Container.PropertySetChangeEvent {
        private static final long serialVersionUID = 6949314954432353421L;

        public Container getContainer() {
            return XYChartRowComponent.this;
        }
    }

}
