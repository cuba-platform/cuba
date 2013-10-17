/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.03.2009 12:33:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.vaadin.data.Property;

public abstract class PropertyAdapter implements Property, Property.ValueChangeNotifier {
    protected final Property itemProperty;

    public PropertyAdapter(Property itemProperty) {
        if (itemProperty == null) throw new IllegalStateException("Property is null");
        this.itemProperty = itemProperty;
    }

    public Class getType() {
        return itemProperty.getType();
    }

    public boolean isReadOnly() {
        return itemProperty.isReadOnly();
    }

    public void setReadOnly(boolean newStatus) {
        itemProperty.setReadOnly(newStatus);
    }

    public void addListener(ValueChangeListener listener) {
        if (itemProperty instanceof ValueChangeNotifier) {
            ((ValueChangeNotifier) itemProperty).addListener(listener);
        }
    }

    public void removeListener(ValueChangeListener listener) {
        if (itemProperty instanceof ValueChangeNotifier) {
            ((ValueChangeNotifier) itemProperty).removeListener(listener);
        }
    }
}
