/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

/**
 * Task executor service
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface BackgroundWorker {

    static final int WATCHDOG_INTERVAL = 100;

    /**
     * Create handler for background task
     * @param task Background task
     * @return Task handler
     */
    <T> BackgroundTaskHandler handle(BackgroundTask<T> task);
}
