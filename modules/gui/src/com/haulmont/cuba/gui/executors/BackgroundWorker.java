/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

/**
 * Task executor service for GUI layer
 *
 * @author artamonov
 * @version $Id$
 */
public interface BackgroundWorker {
    String NAME = "cuba_BackgroundWorker";

    /**
     * Create handler for background task
     *
     * @param <T>  progress measure unit
     * @param task heavy background task
     * @return Task handler
     */
    <T, V> BackgroundTaskHandler<V> handle(BackgroundTask<T, V> task);
}