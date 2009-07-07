/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 18.02.2009 9:55:51
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.WindowContext;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.datatypes.impl.EnumClass;

import java.util.*;
import java.util.List;

public class FrameContext implements WindowContext {
    private final IFrame frame;
    private Map<String, Object> params;

    public FrameContext(IFrame window, Map<String, Object> params) {
        this.frame = window;
        this.params = params;
    }

    public Collection<String> getParameterNames() {
        List<String> names = new ArrayList<String>();
        for (String s : params.keySet()) {
            names.add(s.substring("parameter$".length()));
        }
        return names;
    }

    public <T> T getParameterValue(String property) {
        //noinspection unchecked
        return (T) params.get("parameter$" + property);
    }

    public <T> T getValue(String property) {
        final String[] elements = ValuePathHelper.parse(property);
        String[] path = elements;

        Component component = frame.getComponent(property);
        while (component == null && path.length > 1) {
            final java.util.List<String> subpath = Arrays.asList(elements).subList(0, elements.length - 1);

            path = subpath.toArray(new String[subpath.size()]);
            component = frame.getComponent(ValuePathHelper.format(path));
        }

        if (component == null) return null;

        final Object value = getValue(component);
        if (value == null) return null;

        if (path.length == elements.length) {
            //noinspection unchecked
            return (T) value;
        } else {
            final java.util.List<String> propertyPath = Arrays.asList(elements).subList(path.length, elements.length);
            final String[] properties = propertyPath.toArray(new String[propertyPath.size()]);

            if (value instanceof Instance) {
                //noinspection RedundantTypeArguments
                return InstanceUtils.<T>getValueEx(((Instance) value), properties);
            } else if (value instanceof EnumClass) {
                if (properties.length == 1 && "id".equals(properties[0])) {
                    //noinspection unchecked
                    return (T) ((EnumClass) value).getId();
                } else {
                    throw new UnsupportedOperationException(String.format("Can't get property '%s' of enum %s", propertyPath, value));
                }
            } else {
                return null;
            }
        }
    }

    protected <T> T getValue(Component component) {
        if (component instanceof Component.Field) {
            //noinspection RedundantTypeArguments
            return ((Component.Field) component).<T>getValue();
        } else if (component instanceof List) {
            com.haulmont.cuba.gui.components.List list = (com.haulmont.cuba.gui.components.List) component;
            //noinspection unchecked
            return list.isMultiSelect() ? (T)list.getSelected() : (T)list.getSingleSelected();
        } else {
            return null;
        }
    }

    public void setValue(String property, Object value) {
        final Component component = frame.getComponent(property);
        if (component instanceof Component.Field) {
            ((Component.Field) component).setValue(value);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void addValueListener(ValueListener listener) {
    }

    public void removeValueListener(ValueListener listener) {
    }
}
