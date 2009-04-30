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
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.itmill.toolkit.ui.OptionGroup;
import com.itmill.toolkit.data.Property;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OptionsGroup
    extends
        AbstractOptionsField<OptionGroup>
    implements
        com.haulmont.cuba.gui.components.OptionsGroup, Component.Wrapper
{
    public OptionsGroup() {
        component = new OptionGroup() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(new PropertyAdapter(newDataSource) {
                    public Object getValue() {
                        final Object o = itemProperty.getValue();
                        return getKeyFromValue(o);
                    }

                    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                        final Object v = getValueFromKey(newValue);
                        itemProperty.setValue(v);
                    }
                });
            }
        };
        attachListener(component);
        component.setImmediate(true);
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

    @SuppressWarnings({"unchecked"})
    protected <T> T getValueFromKey(Object key) {
        if (key instanceof Collection) {
            final Set<Object> set = new HashSet<Object>();
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

    protected <T> Object getValue(Object o) {
        Object t;
        if (o instanceof Enum) {
            t = o;
        } else if (o instanceof Entity) {
            t = o;
        } else {
            t = optionsDatasource.getItem(o);
        }
        return t;
    }

    @SuppressWarnings({"unchecked"})
    protected <T> T wrapAsCollection(Object o) {
        if (isMultiSelect()) {
            if (o != null) {
                return (T) Collections.singleton(o);
            } else {
                return (T) Collections.emptySet();
            }
        } else {
            return (T) o;
        }
    }

    @Override
    public void setValue(Object value) {
        // TODO (abramov) need to be changed
        super.setValue(getKeyFromValue(value));
    }

    protected Object getKeyFromValue(Object value) {
        Object v;
        if (isMultiSelect()) {
            if (value instanceof Collection) {
                final Set<Object> set = new HashSet<Object>();
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
            t = ((Entity) o).getId();
        } else if (o instanceof Enum) {
            t = o;
        } else {
            t = o;
        }
        return t;
    }
}
