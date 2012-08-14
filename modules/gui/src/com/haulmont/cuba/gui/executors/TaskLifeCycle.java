/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import java.util.Map;

/**
 * Life cycle object for task
 *
 * @param <T> measure unit which shows progress of task
 * @author artamonov
 * @version $Id$
 */
public interface TaskLifeCycle<T> {

    /**
     * Publish changes from working thread
     *
     * @param changes Changes
     */
    @SuppressWarnings({"unchecked"})
    void publish(T... changes);

    /**
     * @return True if working thread is interrupted
     */
    boolean isInterrupted();

    /**
     * @return Read-only run parameters
     */
    Map<String, Object> getParams();
}