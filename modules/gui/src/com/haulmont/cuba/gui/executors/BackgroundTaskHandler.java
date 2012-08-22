/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

/**
 * Task handler for {@link BackgroundTask}
 *
 * @author artamonov
 * @version $Id$
 */
public interface BackgroundTaskHandler<V> {

    /**
     * Execute<br/>
     * If the task appears to hang then it will be canceled
     */
    void execute();

    /**
     * Try to cancel task
     *
     * @return True if canceled
     */
    boolean cancel();

    /**
     * Synchronous get result from execution
     *
     * @return Result
     */
    V getResult();

    /**
     * Done flag
     *
     * @return True if task is already done
     */
    boolean isDone();

    /**
     * Canceled flag
     *
     * @return True if task has been canceled
     */
    boolean isCancelled();

    /**
     * Alive flag
     *
     * @return True if task is running
     */
    boolean isAlive();
}