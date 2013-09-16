/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.gui.AppConfig;

/**
 * Simple wrapper to a {@link BackgroundTask} to support restarting execution of the same task.
 *
 * @author artamonov
 * @version $Id$
 */
@SuppressWarnings("unused")
public class BackgroundTaskWrapper<T, V> {

    private BackgroundTask<T, V> task;

    private BackgroundTaskHandler<V> taskHandler;
    private BackgroundWorker backgroundWorker;

    public BackgroundTaskWrapper(BackgroundTask<T, V> task) {
        this.task = task;
        this.backgroundWorker = AppConfig.getBackgroundWorker();
    }

    public void restart() {
        cancel();

        taskHandler = backgroundWorker.handle(task);
        taskHandler.execute();
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