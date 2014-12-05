/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data;

import javax.annotation.Nullable;

/**
 * Listener to filter value in change events
 *
 * @deprecated Use normal {@link com.haulmont.cuba.gui.data.ValueListener} with setValue
 *
 * @param <T> type of event source
 * @author artamonov
 * @version $Id$
 */
@Deprecated
public interface ValueChangingListener<T> {

    /**
     * Filter new value
     *
     * @deprecated Use normal {@link com.haulmont.cuba.gui.data.ValueListener} with setValue
     *
     * @param source    Source
     * @param property  Property
     * @param prevValue Previous value
     * @param value     New value
     * @return Filtered value
     */
    @Nullable
    @Deprecated
    Object valueChanging(T source, String property, @Nullable Object prevValue, @Nullable Object value);
}