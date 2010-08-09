/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 05.08.2010 17:08:49
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TwinColumn;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.TwinColSelect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WebTwinColumn
        extends
            WebAbstractOptionsField<TwinColSelect>
        implements
            TwinColumn, Component.Wrapper
{

    private Object nullOption;
    
    private static final long serialVersionUID = 9028516889145414298L;

    public WebTwinColumn() {
        component = new TwinColSelect() {
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
        component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
        component.setMultiSelect(true);
    }

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
    public void setValue(Object value) {
        //todo gorodnov: need to be changed as in WebOptionsGroup
        super.setValue(getKeyFromValue(value));
    }

    public Object getNullOption() {
        return nullOption;
    }

    public void setNullOption(Object nullOption) {
        this.nullOption = nullOption;
        component.setNullSelectionItemId(nullOption);
    }

    @Override
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

    protected Object getValue(Object o) {
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

    @Override
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
