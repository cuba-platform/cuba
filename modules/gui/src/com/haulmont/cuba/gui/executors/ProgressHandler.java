/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface ProgressHandler<T> {

    /**
     * Handle changes from working thread
     * @param changes Changes
     */
    void handleProgress(T ... changes);
}
