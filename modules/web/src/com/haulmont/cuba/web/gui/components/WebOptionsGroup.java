/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.CubaOptionGroup;
import com.haulmont.cuba.web.toolkit.ui.client.optiongroup.OptionGroupOrientation;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractSelect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author abramov
 * @version $Id$
 */
public class WebOptionsGroup
        extends
            WebAbstractOptionsField<CubaOptionGroup>
        implements
            OptionsGroup {

    protected Orientation orientation = Orientation.VERTICAL;

    public WebOptionsGroup() {
        component = new CubaOptionGroup() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(new PropertyAdapter(newDataSource) {

                    @Override
                    public Object getValue() {
                        final Object o = itemProperty.getValue();
                        return getKeyFromValue(o);
                    }

                    @Override
                    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
                        final Object v = getValueFromKey(newValue);
                        itemProperty.setValue(v);
                    }
                });
            }
        };
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);
        component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> T getValue() {
        if (optionsDatasource != null) {
            final Object key = super.getValue();
            return getValueFromKey(key);
        } else {
            return wrapAsCollection(super.getValue());
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
            return wrapAsCollection(o);
        }
    }

    protected Object getValue(Object o) {
        Object t;
        if (o instanceof Enum) {
            t = o;
        } else if (o instanceof Entity) {
            t = o;
        } else if (optionsDatasource != null) {
            if (Datasource.State.INVALID == optionsDatasource.getState()) {
                optionsDatasource.refresh();
            }
            t = optionsDatasource.getItem(o);
        } else {
            t = null;
        }
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
        checkNotNull(orientation, "Orientation must not be null");

        if (orientation != this.orientation) {
            if (orientation == Orientation.HORIZONTAL) {
                component.setOrientation(OptionGroupOrientation.HORIZONTAL);
            } else {
                component.setOrientation(OptionGroupOrientation.VERTICAL);
            }
            this.orientation = orientation;
        }
    }
}