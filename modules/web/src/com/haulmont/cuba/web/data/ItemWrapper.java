/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 17:01:30
 * $Id$
 */
package com.haulmont.cuba.web.data;

import com.haulmont.chile.core.model.MetaProperty;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemWrapper implements Item {
    private Map<MetaProperty, PropertyWrapper> properties = new HashMap<MetaProperty, PropertyWrapper>();

    public ItemWrapper(Object item, Collection<MetaProperty> properties) {
        for (MetaProperty property : properties) {
            this.properties.put(property, new PropertyWrapper(item, property));
        }
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
}
