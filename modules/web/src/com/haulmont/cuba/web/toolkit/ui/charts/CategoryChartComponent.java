package com.haulmont.cuba.web.toolkit.ui.charts;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;

import java.io.Serializable;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public abstract class CategoryChartComponent extends ChartComponent implements CategoryChart {

    private static final long serialVersionUID = -2096187829656779886L;

    protected Container items;
    protected Object rowCaptionPropertyId;

    protected Map<Object, String> categories = new HashMap<Object, String>();

    protected Set<ItemSetChangeListener> itemSetChangeListeners = new LinkedHashSet<ItemSetChangeListener>();
    protected Set<PropertySetChangeListener> propertySetChangeListeners = new LinkedHashSet<PropertySetChangeListener>();

    protected CategoryChartComponent() {
        setContainerDataSource(new IndexedContainer());
    }

    public Collection<?> getRowIds() {
        return getItemIds();
    }

    public void addRow(Object id, String caption) {
        addItem(id);

        if (rowCaptionPropertyId != null) {
            Property p = getContainerProperty(id, rowCaptionPropertyId);
            if (p != null) {
                p.setValue(caption);
            }
        }
    }

    public String getRowCaption(Object id) {
        String caption = null;

        if (rowCaptionPropertyId != null) {
            Property p = getContainerProperty(id, rowCaptionPropertyId);
            if (p != null) {
                caption = p.toString();
            }
        } else {
            Item i = getItem(id);
            if (i != null) {
                caption = i.toString();
            }
        }

        return caption == null ? "" : caption;
    }

    public Object getRowCaptionPropertyId() {
        return rowCaptionPropertyId;
    }

    public void setRowCaptionPropertyId(Object propertyId) {
        rowCaptionPropertyId = propertyId;
        requestRepaint();
    }

    public Collection<?> getPropertyIds() {
        return getContainerPropertyIds();
    }

    public void addProperty(Object id) {
        addContainerProperty(id, Object.class, null);
    }

    public Collection<?> getCategoryPropertyIds() {
        return categories.keySet();
    }

    public void addCategory(Object propertyId, String caption) {
        categories.put(propertyId, caption);
        requestRepaint();
    }

    public String getCategoryCaption(Object propertyId) {
        String caption = categories.get(propertyId);
        if (caption == null) {
            caption = propertyId.toString();
        }
        return caption == null ? "" : caption;
    }

    public Object getValue(Object rowId, Object propertyId) {
        Property p = getContainerProperty(rowId, propertyId);
        if (p == null) {
            return null;
        }

        Object o = p.getValue();
        return o;
    }

    public void setValue(Object rowId, Object propertyId, Object value) {
        Property p = getContainerProperty(rowId, propertyId);
        if (p != null) {
            p.setValue(value);
        }
    }

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
        itemSetChangeListeners.remove(listener);
    }

    public void containerPropertySetChange(Container.PropertySetChangeEvent event) {
        firePropertySetChange();
    }

    public void addListener(PropertySetChangeListener listener) {
        propertySetChangeListeners.add(listener);
    }

    public void removeListener(PropertySetChangeListener listener) {
        if (propertySetChangeListeners != null) {
            propertySetChangeListeners.remove(listener);
        }
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

    private class ItemSetChangeEvent implements Serializable, Container.ItemSetChangeEvent {
        private static final long serialVersionUID = -3105286428491462551L;

        public Container getContainer() {
            return CategoryChartComponent.this;
        }
    }

    private class PropertySetChangeEvent implements Serializable, Container.PropertySetChangeEvent {
        private static final long serialVersionUID = -1597899704080413194L;

        public Container getContainer() {
            return CategoryChartComponent.this;
        }
    }
}
