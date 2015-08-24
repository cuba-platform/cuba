/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class EnumerationContainer implements com.vaadin.data.Container, Container.ItemSetChangeNotifier {

    protected List<Enum> values;
    // lazily initialized listeners list
    protected List<ItemSetChangeListener> itemSetChangeListeners = null;
    protected boolean ignoreListeners;

    public EnumerationContainer(List<Enum> values) {
        this.values = values;
    }

    @Override
    public Item getItem(Object itemId) {
        return new EnumerationItem(itemId);
    }

    @Override
    public Collection getContainerPropertyIds() {
        return Collections.emptyList();
    }

    @Override
    public Collection getItemIds() {
        return values;
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class getType(Object propertyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        //noinspection SuspiciousMethodCalls
        return values.contains(itemId);
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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners == null) {
            itemSetChangeListeners = new LinkedList<>();
        }

        itemSetChangeListeners.add(listener);
    }

    @Override
    public void addListener(ItemSetChangeListener listener) {
        addItemSetChangeListener(listener);
    }

    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners != null) {
            itemSetChangeListeners.remove(listener);

            if (itemSetChangeListeners.isEmpty()) {
                itemSetChangeListeners = null;
            }
        }
    }

    @Override
    public void removeListener(ItemSetChangeListener listener) {
        removeItemSetChangeListener(listener);
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

        if (itemSetChangeListeners != null) {
            StaticItemSetChangeEvent event = new StaticItemSetChangeEvent(this);

            for (ItemSetChangeListener listener : itemSetChangeListeners) {
                listener.containerItemSetChange(event);
            }
        }

        ignoreListeners = false;
    }

    protected static class EnumerationItem implements Item {
        protected Enum item;
        protected String name;

        public EnumerationItem(Object itemId) {
            this.item = (Enum) itemId;

            Messages messages = AppBeans.get(Messages.NAME);
            name = messages.getMessage(item);
            if (StringUtils.isEmpty(name)) {
                name = item.toString();
            }
        }

        @Override
        public Property getItemProperty(Object id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection getItemPropertyIds() {
            return Collections.emptyList();
        }

        @Override
        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}