/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class FrameContext implements WindowContext {

    private static final long serialVersionUID = -6616685746440637906L;

    private final IFrame frame;
    private Map<String, Object> params;

    public FrameContext(IFrame window, Map<String, Object> params) {
        this.frame = window;
        this.params = params;

        frame.getComponents();
    }

    public Collection<String> getParameterNames() {
        List<String> names = new ArrayList<String>();
        for (String s : params.keySet()) {
            names.add(s.substring("param$".length()));
        }
        return names;
    }

    public <T> T getParameterValue(String property) {
        //noinspection unchecked
        return (T) params.get("param$" + property);
    }

    @Override
    public IFrame getFrame() {
        return frame;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public <T> T getParamValue(String param) {
        return (T) params.get(param);
    }

    @Override
    public <T> T getValue(String property) {
        final String[] elements = ValuePathHelper.parse(property);
        String[] path = elements;

        Component component = frame.getComponent(property);
        while (component == null && path.length > 1) {
            // in case of property contains a drill-down part
            path = (String[]) ArrayUtils.subarray(path, 0, path.length - 1);
            component = frame.getComponent(ValuePathHelper.format(path));
        }

        if (component == null || component == frame
                || ((component instanceof Component.Wrapper) && ((Component.Wrapper) component).getComponent() == frame))
        {
            // if component not found or found the frame itself, try to search in parent frame
            if (frame.getFrame() != null)
                return frame.getFrame().getContext().<T>getValue(property);
            else
                return null;
        }

        final Object value = getValue(component);
        if (value == null)
            return null;

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
        if (component instanceof Component.HasValue) {
            //noinspection RedundantTypeArguments
            return ((Component.HasValue) component).<T>getValue();
        } else if (component instanceof ListComponent) {
            ListComponent list = (ListComponent) component;
            //noinspection unchecked
            return list.isMultiSelect() ? (T)list.getSelected() : (T)list.getSingleSelected();
        } else {
            return null;
        }
    }

    @Override
    public void setValue(String property, Object value) {
        final Component component = frame.getComponent(property);
        if (component instanceof Component.HasValue) {
            ((Component.HasValue) component).setValue(value);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void addValueListener(String componentName, ValueListener listener) {
        Component component = frame.getComponent(componentName);
        if (component == null)
            throw new RuntimeException("Component not found: " + componentName);
        if (component instanceof Component.HasValue) {
            ((Component.HasValue) component).addListener(listener);
        } else if (component instanceof ListComponent) {
            throw new UnsupportedOperationException("List component is not supported yet");
        } else {
            throw new RuntimeException("Unable to add listener to the component " + component);
        }
    }

    @Override
    public void removeValueListener(String componentName, ValueListener listener) {
        Component component = frame.getComponent(componentName);
        if (component == null)
            throw new RuntimeException("Component not found: " + componentName);
        if (component instanceof Component.HasValue) {
            ((Component.HasValue) component).removeListener(listener);
        } else if (component instanceof ListComponent) {
            throw new UnsupportedOperationException("List component is not supported yet");
        } else {
            throw new RuntimeException("Unable to add listener to the component " + component);
        }
    }
}
