/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import java.util.concurrent.TimeUnit;

/**
 * Task handler for {@link BackgroundTask}
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface BackgroundTaskHandler<V> {

    /**
     * Execute with timeout <br/>
     * If the task appears to hang then it will be canceled
     *
     * @param timeout Timeout
     * @param unit TimeUnit
     */
    void execute(long timeout, TimeUnit unit);

    /**
     * Try to cancel task
     * @param mayInterruptIfRunning Interrupt if running
     * @return True if canceled
     */
    boolean cancel(boolean mayInterruptIfRunning);

    /**
     * Synchronous get result from execution
     * @return Result
     */
    V getResult();

    /**
     * Done flag
     * @return True if task is already done
     */
    boolean isDone();

    /**
     * Canceled flag
     * @return True if task has been canceled
     */
    boolean isCancelled();

    /**
     * Alive flag
     * @return True if task is running
     */
    boolean isAlive();
}