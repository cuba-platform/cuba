/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Abramov
 * @version $Id$
 */
public class EnumerationContainer implements com.vaadin.data.Container {

    private List values;

    public EnumerationContainer(List values) {
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
        private Enum item;
        private String name;

        public EnumerationItem(Object itemId) {
            this.item = (Enum) itemId;

            name = AppBeans.get(Messages.class).getMessage(item);
            if (StringUtils.isEmpty(name))
                name = item.toString();
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
