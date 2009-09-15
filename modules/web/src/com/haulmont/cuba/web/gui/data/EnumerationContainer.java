/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.03.2009 13:41:01
 * $Id$
 */
package com.haulmont.cuba.web.gui.data;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.haulmont.cuba.core.global.MessageProvider;

import java.util.List;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;

public class EnumerationContainer implements com.vaadin.data.Container {
    private List<Enum> values;

    public EnumerationContainer(List<Enum> values) {
        this.values = values;
    }

    public Item getItem(Object itemId) {
        return new EnumerationItem(itemId);
    }

    public Collection getContainerPropertyIds() {
        return Collections.emptyList();
    }

    public Collection getItemIds() {
        return values;
    }

    public Property getContainerProperty(Object itemId, Object propertyId) {
        throw new UnsupportedOperationException();
    }

    public Class getType(Object propertyId) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return values.size();
    }

    public boolean containsId(Object itemId) {
        return values.contains(itemId);
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
        throw new UnsupportedOperationException();
    }

    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    private static class EnumerationItem implements Item {
        private Object item;
        private String name;

        public EnumerationItem(Object itemId) {
            this.item = itemId;
            String nameKey = item.getClass().getSimpleName() + "." + item.toString();

            name = MessageProvider.getMessage(item.getClass(), nameKey);
            if (StringUtils.isEmpty(name)) name = item.toString();
        }

        public Property getItemProperty(Object id) {
            throw new UnsupportedOperationException();
        }

        public Collection getItemPropertyIds() {
            return Collections.emptyList();
        }

        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
