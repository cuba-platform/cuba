/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.09.2010 9:34:03
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

import java.io.Serializable;
import java.util.*;

public abstract class ChartComponent extends AbstractComponent implements
        Container, Container.Viewer,
        Container.ItemSetChangeNotifier, Container.ItemSetChangeListener,
        Container.PropertySetChangeNotifier, Container.PropertySetChangeListener {

    protected Container items;

    private Map<Object, String> rowCaptions = new HashMap<Object, String>();
    private Map<Object, String> columnCaptions = new HashMap<Object, String>();

    private Set<ItemSetChangeListener> itemSetChangeListeners;
    private Set<PropertySetChangeListener> propertySetChangeListeners;

    private Object rowCaptionPropertyId;

    private boolean hasLegend;

    private String columnAxisLabel;
    private String valueAxisLabel;

    private int chartWidth;
    private int chartHeight;

    private boolean has3D;

    private Chart.Orientation orientation;

    private List<Object> columnPropertyIds = new ArrayList<Object>();

    protected ChartComponent() {
        this(null, new IndexedContainer());
    }

    protected ChartComponent(String caption) {
        this(caption, new IndexedContainer());
    }

    protected ChartComponent(Container datasource) {
        this(null, datasource);
    }

    protected ChartComponent(String caption, Container datasource) {
        setCaption(caption);
        setContainerDataSource(datasource);
        setChartWidth(400);
        setChartHeight(300);
    }

    public void setContainerDataSource(Container datasource) {
        if (datasource == null) {
            datasource = new IndexedContainer();
        }
          //todo gorodnov: implement captionChageListener
//        getCaptionChangeListener().clear();

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

            final Collection<?> propertyIds = getContainerPropertyIds();
            for (final Object id : propertyIds) {
                if (!id.equals(getRowCaptionPropertyId())) {
                    columnPropertyIds.add(id);
                }
            }

            if (items instanceof ItemSetChangeNotifier) {
                ((ItemSetChangeNotifier) items).addListener(this);
            }
            if (items instanceof PropertySetChangeNotifier) {
                ((PropertySetChangeNotifier) items).addListener(this);
            }

            requestRepaint();
        }
    }

    public void addColumnProperty(Object propertyId, Class<?> classType) {
        addContainerProperty(propertyId, classType, null);
        columnPropertyIds.add(propertyId);
    }

    public Collection<?> getColumnPropertyIds() {
        return columnPropertyIds;
    }

    public String getColumnCaption(Object propertyId) {
        String caption = null;
        if (propertyId != null) {
            caption = columnCaptions.get(propertyId);
            if (caption == null) {
                caption = propertyId.toString();
            }
        }
        return caption == null ? "" : caption;
    }

    public void setColumnCaption(Object propertyId, String caption) {
        if (getColumnPropertyIds().contains(propertyId)) {
            columnCaptions.put(propertyId, caption);
            requestRepaint();
        }
    }

    public Property getColumnProperty(Object itemId, Object propertyId) {
        return getContainerProperty(itemId, propertyId);
    }

    public Number getColumnValue(Object itemId, Object propertyId) {
        Property p = getColumnProperty(itemId, propertyId);
        if (p != null) {
            Object o = p.getValue();
            if (o instanceof Number) {
                return ((Number) o).doubleValue();
            }
        }
        return null;
    }

    public Object addRow(String caption) {
        Object itemId = addItem();
        rowCaptions.put(itemId, caption);
        return itemId;
    }

    public Item addRow(Object itemId, String caption) {
        Item item = addItem(itemId);
        rowCaptions.put(itemId, caption);
        return item;
    }

    public Object addRow(Object[] values, Object itemId, String caption) {
        final List<Object> propertyIds = new ArrayList<Object>(getContainerPropertyIds());
        if (values.length != propertyIds.size()) {
            return null;
        }

        Item item;
        if (itemId == null) {
            itemId = addRow(caption);
            if (itemId == null) {
                return null;
            }
            item = getItem(itemId);
        } else {
            item = addRow(itemId, caption);
        }
        if (item == null) {
            return null;
        }

        for (int i = 0; i < propertyIds.size(); i++) {
            item.getItemProperty(propertyIds.get(i)).setValue(values[i]);
        }

        requestRepaint();
        
        return itemId;
    }

    public Collection<?> getRowIds() {
        return getItemIds();
    }

    public Object getRowCaptionPropertyId() {
        return rowCaptionPropertyId;
    }

    public void setRowCaptionPropertyId(Object rowCaptionPropertyId) {
        this.rowCaptionPropertyId = rowCaptionPropertyId;
        columnPropertyIds.remove(rowCaptionPropertyId);
    }

    public String getRowCaption(Object itemId) {
        String caption = null;
        if (itemId != null) {
            caption = rowCaptions.get(itemId);
            if (caption == null) {
                if (getRowCaptionPropertyId() != null) {
                    Property p = getContainerProperty(itemId, getRowCaptionPropertyId());
                    if (p != null) {
                        caption = p.toString();
                    }
                } else {
                    Item item = getItem(itemId);
                    caption = item.toString();
                }
            }
        }
        return caption == null ? "" : caption;
    }

    public Item getRow(Object itemId) {
        return getItem(itemId);
    }

    public String getColumnAxisLabel() {
        return columnAxisLabel;
    }

    public void setColumnAxisLabel(String label) {
        columnAxisLabel = label;
    }

    public String getValueAxisLabel() {
        return valueAxisLabel;
    }

    public void setValueAxisLabel(String label) {
        valueAxisLabel = label;
    }

    public int getChartWidth() {
        return chartWidth;
    }

    public void setChartWidth(int chartWidth) {
        this.chartWidth = chartWidth;
        setWidth(chartWidth + "px");
    }

    public int getChartHeight() {
        return chartHeight;
    }

    public void setChartHeight(int chartHeight) {
        this.chartHeight = chartHeight;
        setHeight(chartHeight + "px");
    }

    public boolean isLegend() {
        return hasLegend;
    }

    public void setLegend(boolean legend) {
        this.hasLegend = legend;
        requestRepaint();
    }

    public boolean is3D() {
        return has3D;
    }

    public void set3D(boolean has3D) {
        this.has3D = has3D;
    }

    public Chart.Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Chart.Orientation orientation) {
        this.orientation = orientation;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (isLegend()) {
            target.addAttribute("legend", true);
        }
        target.addAttribute("cwidth", getChartWidth());
        target.addAttribute("cheight", getChartHeight());
    }

    public Container getContainerDataSource() {
        return items;
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
            if (itemSetChangeListeners.isEmpty()) {
                itemSetChangeListeners = null;
            }
        }
    }

    public void containerPropertySetChange(Container.PropertySetChangeEvent event) {
        firePropertySetChange();
    }

    public void addListener(PropertySetChangeListener listener) {
        if (propertySetChangeListeners == null) {
            propertySetChangeListeners = new LinkedHashSet<PropertySetChangeListener>();
        }
        propertySetChangeListeners.add(listener);
    }

    public void removeListener(PropertySetChangeListener listener) {
        if (propertySetChangeListeners != null) {
            propertySetChangeListeners.remove(listener);
            if (propertySetChangeListeners.isEmpty()) {
                propertySetChangeListeners = null;
            }
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
        return itemId != null && items.containsId(itemId);
    }

    public Item addItem(Object itemId) throws UnsupportedOperationException {
        final Item retval = items.addItem(itemId);
        if (retval != null
                && !(items instanceof Container.ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    public Object addItem() throws UnsupportedOperationException {
        final Object retval = items.addItem();
        if (retval != null
                && !(items instanceof Container.ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        final boolean retval = items.removeItem(itemId);
        if (retval && !(items instanceof Container.ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        final boolean retval = items.addContainerProperty(propertyId, type,
                defaultValue);
        if (retval && !(items instanceof Container.PropertySetChangeNotifier)) {
            firePropertySetChange();
        }
        return retval;
    }

    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        final boolean retval = items.removeContainerProperty(propertyId);
        if (retval && !(items instanceof Container.PropertySetChangeNotifier)) {
            firePropertySetChange();
        }
        return retval;
    }

    public boolean removeAllItems() throws UnsupportedOperationException {
        final boolean retval = items.removeAllItems();
        if (retval) {
            if (!(items instanceof Container.ItemSetChangeNotifier)) {
                fireItemSetChange();
            }
        }
        return retval;
    }

    private void fireItemSetChange() {
        if (itemSetChangeListeners != null && !itemSetChangeListeners.isEmpty()) {
            for (final ItemSetChangeListener listener : itemSetChangeListeners) {
                listener.containerItemSetChange(new ItemSetChangeEvent());
            }
        }
        requestRepaint();
    }

    private void firePropertySetChange() {
        if (propertySetChangeListeners != null && !propertySetChangeListeners.isEmpty()) {
            for (final PropertySetChangeListener listener : propertySetChangeListeners) {
                listener.containerPropertySetChange(new PropertySetChangeEvent());
            }
        }
        requestRepaint();
    }

    private class ItemSetChangeEvent implements Serializable,
            Container.ItemSetChangeEvent {
        private static final long serialVersionUID = -3105286428491462551L;

        public Container getContainer() {
            return ChartComponent.this;
        }
    }

    private class PropertySetChangeEvent implements Serializable,
            Container.PropertySetChangeEvent {
        private static final long serialVersionUID = -1597899704080413194L;

        public Container getContainer() {
            return ChartComponent.this;
        }
    }
}
