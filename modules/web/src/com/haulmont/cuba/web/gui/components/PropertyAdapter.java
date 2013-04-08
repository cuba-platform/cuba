/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.vaadin.data.Property;

/**
 * @author abramov
 * @version $Id$
 */
public abstract class PropertyAdapter implements Property, Property.ValueChangeNotifier {
    protected final Property itemProperty;

    public PropertyAdapter(Property itemProperty) {
        if (itemProperty == null) throw new IllegalStateException("Property is null");
        this.itemProperty = itemProperty;
    }

    @Override
    public Class getType() {
        return itemProperty.getType();
    }

    @Override
    public boolean isReadOnly() {
        return itemProperty.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean newStatus) {
        itemProperty.setReadOnly(newStatus);
    }

    @Override
    public void addListener(ValueChangeListener listener) {
        addValueChangeListener(listener);
    }

    @Override
    public void removeListener(ValueChangeListener listener) {
        removeValueChangeListener(listener);
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        if (itemProperty instanceof ValueChangeNotifier) {
            ((ValueChangeNotifier) itemProperty).addValueChangeListener(listener);
        }
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        if (itemProperty instanceof ValueChangeNotifier) {
            ((ValueChangeNotifier) itemProperty).removeValueChangeListener(listener);
        }
    }
}