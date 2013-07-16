/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data;

import javax.annotation.Nullable;

/**
 * Listener to filter value in change events
 *
 * @param <T> type of event source
 * @author artamonov
 * @version $Id$
 */
public interface ValueChangingListener<T> {

    /**
     * Filter new value
     *
     * @param source    Source
     * @param property  Property
     * @param prevValue Previous value
     * @param value     New value
     * @return Filtered value
     */
    @Nullable
    Object valueChanging(T source, String property, @Nullable Object prevValue, @Nullable Object value);
}