/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.data;

import javax.annotation.Nullable;

/**
 * Listener to value change events
 *
 * @param <T> type of event source
 * @author abramov
 * @version $Id$
 */
public interface ValueListener<T> {
    void valueChanged(T source, String property, @Nullable Object prevValue, @Nullable Object value);
}