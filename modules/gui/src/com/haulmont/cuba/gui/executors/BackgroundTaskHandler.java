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

/**
 * Task handler for {@link BackgroundTask}.
 *
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