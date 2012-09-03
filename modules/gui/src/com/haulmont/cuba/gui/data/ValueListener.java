/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 12:37:09
 * $Id$
 */
package com.haulmont.cuba.gui.data;

/**
 * Listener to value change events
 * @param <T> type of event source 
 */
public interface ValueListener<T> {
    void valueChanged(T source, String property, Object prevValue, Object value);
}
