/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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