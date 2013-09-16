/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.executors;

/**
 * Entry point to {@link BackgroundTask} execution functionality.
 *
 * @author artamonov
 * @version $Id$
 */
public interface BackgroundWorker {

    String NAME = "cuba_BackgroundWorker";

    /**
     * Create handler for a background task. The handler is used to control the task execution.
     *
     * @param <T>  progress measure unit
     * @param <V>  task result type
     * @param task background task instance
     * @return task handler
     */
    <T, V> BackgroundTaskHandler<V> handle(BackgroundTask<T, V> task);
}