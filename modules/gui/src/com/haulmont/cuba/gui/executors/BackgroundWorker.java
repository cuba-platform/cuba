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
 * Entry point to {@link BackgroundTask} execution functionality.
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
     * @throws IllegalConcurrentAccessException in case of call from non UI thread
     */
    @ExecutedOnUIThread
    <T, V> BackgroundTaskHandler<V> handle(BackgroundTask<T, V> task);

    /**
     * Obtain UI access for later use from background thread. Can be invoked only from UI thread.
     *
     * @return ui accessor object that allows to read/write state of UI
     * @throws IllegalConcurrentAccessException in case of call from non UI thread
     */
    @ExecutedOnUIThread
    UIAccessor getUIAccessor();

    /**
     * @throws IllegalConcurrentAccessException in case of call from non UI thread
     */
    void checkUIAccess();
}