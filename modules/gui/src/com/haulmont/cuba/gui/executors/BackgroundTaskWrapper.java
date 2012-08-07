/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.gui.AppConfig;

import java.util.concurrent.TimeUnit;

/**
 * @author artamonov
 * @version $Id$
 */
@SuppressWarnings("unused")
public class BackgroundTaskWrapper<T, V> {

    private BackgroundTask<T, V> task;
    private long timeout;
    private TimeUnit timeUnit;

    private BackgroundTaskHandler<V> taskHandler;
    private BackgroundWorker backgroundWorker;

    public BackgroundTaskWrapper(BackgroundTask<T, V> task, long timeout, TimeUnit timeUnit) {
        this.task = task;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.backgroundWorker = AppConfig.getBackgroundWorker();
    }

    public void restart() {
        cancel();

        taskHandler = backgroundWorker.handle(task);
        taskHandler.execute(timeout, timeUnit);
    }

    public V getResult() {
        if (taskHandler != null)
            return taskHandler.getResult();
        else
            return null;
    }

    public void cancel() {
        if (taskHandler != null)
            taskHandler.cancel();
    }
}