/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data;

/**
 * Listener to filter value in change events
 *
 * @param <T> type of event source
 * <p>$Id$</p>
 *
 * @author artamonov
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
    Object valueChanging(T source, String property, Object prevValue, Object value);
}