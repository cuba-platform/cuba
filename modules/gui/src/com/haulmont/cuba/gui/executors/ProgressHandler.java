/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

/**
 * @author artamonov
 * @version $Id$
 */
public interface ProgressHandler<T> {

    /**
     * Handle changes from working thread
     *
     * @param changes Changes
     */
    @SuppressWarnings({"unchecked"})
    void handleProgress(T... changes);
}
