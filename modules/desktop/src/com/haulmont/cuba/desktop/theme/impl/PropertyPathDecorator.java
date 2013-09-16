/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.theme.ComponentDecorator;
import com.haulmont.cuba.gui.components.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class PropertyPathDecorator implements ComponentDecorator {

    protected String[] properties;
    protected Object value;
    protected String state;

    public PropertyPathDecorator(String propertyPath) {
        this(propertyPath, null, null);
    }

    public PropertyPathDecorator(String propertyPath, Object value) {
        this(propertyPath, value, null);
    }

    public PropertyPathDecorator(String propertyPath, Object value, String state) {
        this.properties = propertyPath.split("\\.");
        if (properties.length < 1) {
            throw new IllegalArgumentException("Invalid property path: " + propertyPath);
        }
        this.value = value;
        this.state = state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void decorate(Object component, Set<String> state) {
        if (this.state != null && (state == null || !state.contains(this.state))) {
            return;
        }

        Object object = component;

        if (component instanceof Component && properties.length == 1) {
            object = DesktopComponentsHelper.getComposition((Component) component);
        }

        for (int i = 0; i < properties.length - 1; i++) {
            object = getProperty(object, properties[i]);
        }
        apply(object, properties[properties.length - 1]);
    }

    public Object getProperty(Object object, String property) {
        String methodName = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
        Class componentClass = object.getClass();
        try {
            Method method = componentClass.getMethod(methodName);
            return method.invoke(object);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void apply(Object object, String property) {
        setProperty(object, property, value);
    }

    protected void setProperty(Object object, String property, Object value) {
        String methodName = "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
        Class componentClass = object.getClass();
        Method[] methods = componentClass.getMethods();
        Method method = null;
        for (Method m: methods) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            throw new RuntimeException("Can't find matching method for property " + property + " at object " + object);
        }

        try {
            method.invoke(object, value);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
