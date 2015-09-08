/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import javax.annotation.Nullable;

/**
 * Listener to value change events
 *
 * @deprecated Use {@link com.haulmont.cuba.gui.components.Component.ValueChangeListener}
 * @param <T> type of event source
 * @author abramov
 * @version $Id$
 */
@Deprecated
public interface ValueListener<T> {

    /**
     * Called when an attribute value changed.
     *
     * @param source    changed object
     * @param property  changed attribute name
     * @param prevValue previous value
     * @param value     current value
     */
    void valueChanged(T source, String property, @Nullable Object prevValue, @Nullable Object value);
}