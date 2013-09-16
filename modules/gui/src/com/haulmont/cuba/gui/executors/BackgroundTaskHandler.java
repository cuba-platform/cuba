/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.executors;

/**
 * Task handler for {@link BackgroundTask}.
 *
 * @author artamonov
 * @version $Id$
 */
public interface BackgroundTaskHandler<V> {

    /**
     * Execute the {@link BackgroundTask}.
     * <p/> This method must be called only once for a handler instance.
     */
    void execute();

    /**
     * Cancel task.
     *
     * @return true if canceled, false if the task was not started or is already stopped
     */
    boolean cancel();

    /**
     * Wait for the task completion and return its result.
     *
     * @return task's result returned from {@link BackgroundTask#run(TaskLifeCycle)} method
     */
    V getResult();

    /**
     * @return true if the task is completed
     */
    boolean isDone();

    /**
     * @return true if the task has been canceled
     */
    boolean isCancelled();

    /**
     * @return true if the task is running
     */
    boolean isAlive();
}