/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 12:35:10
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import java.util.Map;

/**
 * Provides access to window parameters and component values
 */
public interface WindowContext {

    /**
     * External parameters passed to the window on opening.
     */
    Map<String, Object> getParams();

    /**
     * Value of an external parameters passed to the window on opening.
     */
    <T> T getParamValue(String param);

    /**
     * Retrieves value of a component by complex name, dereferencing path to the component
     * and possible drill down to the value
     * @param property path to the value. Parsed by the following rules:
     * <br>First split by dots taking into account square brackets, and looking for a component from left to right.
     * <br>If a component not found, return null.
     * <br>If a component found and it is a {@link com.haulmont.cuba.gui.components.Component.HasValue}
     * or {@link com.haulmont.cuba.gui.components.List}, retrieve its value.
     * <br>If the value is null, return it.
     * <br>If there is nothing left in the path after the component name, return the value.
     * <br>Else if the value is {@link com.haulmont.chile.core.model.Instance}, drill down to it and return the value
     * of the property by remaining property path.
     * <br>If the value is an {@link com.haulmont.chile.core.datatypes.impl.EnumClass} and remaining
     * property path is "id", return EnumClass.getId() value.
     */
    <T> T getValue(String property);

    /**
     * Set value of a component by its path in the window
     * @param property path to the component (separated by dots, taking into account square brackets)
     * @param value value to set
     * @throws UnsupportedOperationException if the component not found or is not a {@link com.haulmont.cuba.gui.components.Component.HasValue}
     */
    void setValue(String property, Object value);

    /** Not implemented yet */
    void addValueListener(ValueListener listener);
    /** Not implemented yet */
    void removeValueListener(ValueListener listener);
}
