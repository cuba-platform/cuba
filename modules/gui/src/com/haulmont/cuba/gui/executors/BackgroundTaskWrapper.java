/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.gui.AppConfig;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Provides simple API for cases when the same type of background task
 * gets started, restarted and cancelled repetitively.
 *
 * @see com.haulmont.cuba.gui.executors.BackgroundWorker
 */
@SuppressWarnings("unused")
public class BackgroundTaskWrapper<T, V> {

    private BackgroundTask<T, V> task;

    private BackgroundTaskHandler<V> taskHandler;
    private BackgroundWorker backgroundWorker;

    public BackgroundTaskWrapper(@Nullable BackgroundTask<T, V> task) {
        this.task = task;
        this.backgroundWorker = AppConfig.getBackgroundWorker();
    }

    public BackgroundTaskWrapper() {
        this(null);
    }

    /**
     * Cancel running task if there is at the moment.
     * Launch it again.
     */
    public void restart() {
        cancel();

        Objects.requireNonNull(task, "Task must be specified either in constructor or by passing it to restart() method");
        taskHandler = backgroundWorker.handle(task);
        taskHandler.execute();
    }

    /**
     * Cancel running task if there is at the moment.
     * Launch new task specified as parameter.
     *
     * @param task task to start, also will be saved for future restarts
     */
    public void restart(BackgroundTask<T, V> task) {
        cancel();

        this.task = task;
        taskHandler = backgroundWorker.handle(task);
        taskHandler.execute();
    }

    /**
     * If there is running task, block until its completion and return result.
     *
     * @return last task's result or null if no tasks were run yet
     */
    @Nullable
    public V getResult() {
        if (taskHandler != null)
            return taskHandler.getResult();
        else
            return null;
    }

    /**
     * Cancel running task if there is any.
     */
    public void cancel() {
        if (taskHandler != null)
            taskHandler.cancel();
    }
}