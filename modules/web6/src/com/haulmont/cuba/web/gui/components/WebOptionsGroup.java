/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 16:55:25
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.OptionGroup;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author abramov
 * @version $Id$
 */
public class WebOptionsGroup
        extends
            WebAbstractOptionsField<OptionGroup>
        implements
            OptionsGroup, Component.Wrapper {

    private static final String HORIZONTAL_STYLENAME = "horizontal";

    private Orientation orientation = Orientation.VERTICAL;

    public WebOptionsGroup() {
        component = new OptionGroup() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(new PropertyAdapter(newDataSource) {

                    @Override
                    public Object getValue() {
                        final Object o = itemProperty.getValue();
                        return getKeyFromValue(o);
                    }

                    @Override
                    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                        final Object v = getValueFromKey(newValue);
                        itemProperty.setValue(v);
                    }
                });
            }
        };
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);
        component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> T getValue() {
        if (optionsDatasource != null) {
            final Object key = super.getValue();
            return (T) getValueFromKey(key);
        } else {
            return (T) wrapAsCollection(super.getValue());
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected <T> T getValueFromKey(Object key) {
        if (key instanceof Collection) {
            final Set<Object> set = new HashSet<>();
            for (Object o : (Collection) key) {
                Object t = getValue(o);
                set.add(t);
            }
            return (T) set;
        } else {
            final Object o = getValue(key);
            return (T) wrapAsCollection(o);
        }
    }

    protected Object getValue(Object o) {
        Object t;
        if (o instanceof Enum) {
            t = o;
        } else if (o instanceof Entity) {
            t = o;
        } else if (optionsDatasource != null) {
            t = optionsDatasource.getItem(o);
        } else
            t = null;
        return t;
    }

    @Override
    public void setValue(Object value) {
        // TODO (abramov) need to be changed
        super.setValue(getKeyFromValue(value));
    }

    @Override
    protected Object getKeyFromValue(Object value) {
        Object v;
        if (isMultiSelect()) {
            if (value instanceof Collection) {
                final Set<Object> set = new HashSet<>();
                for (Object o : (Collection) value) {
                    Object t = getKey(o);
                    set.add(t);
                }
                v = set;
            } else {
                v = getKey(value);
            }
        } else {
            v = getKey(value);
        }

        return v;
    }

    protected Object getKey(Object o) {
        Object t;
        if (o instanceof Entity) {
            if (optionsDatasource != null) {
                if (Datasource.State.INVALID == optionsDatasource.getState()) {
                    optionsDatasource.refresh();
                }
                return ((Entity) o).getId();
            } else {
                if ((optionsList != null) || (optionsMap != null))
                    return o;
                return ((Entity) o).getId();
            }
        } else if (o instanceof Enum) {
            t = o;
        } else {
            t = o;
        }
        return t;
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        if (orientation == null) {
            throw new IllegalArgumentException("Orientation must not be null");
        }
        if (orientation != this.orientation) {
            updateComponentStyle(orientation);
        }
        this.orientation = orientation;
    }

    private void updateComponentStyle(Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            component.addStyleName(HORIZONTAL_STYLENAME);
        }
        else {
            component.removeStyleName(HORIZONTAL_STYLENAME);
        }
    }
}
