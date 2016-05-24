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

package com.haulmont.cuba.gui.executors.impl;

import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.ExecutedOnUIThread;

public interface TaskExecutor<T, V> {
    @ExecutedOnUIThread
    void startExecution();

    @ExecutedOnUIThread
    boolean cancelExecution();

    @ExecutedOnUIThread
    V getResult();

    BackgroundTask<T, V> getTask();

    boolean isCancelled();

    boolean isDone();

    boolean inProgress();

    /**
     * Done handler for clear resources
     *
     * @param finalizer Runnable handler
     */
    void setFinalizer(Runnable finalizer);

    Runnable getFinalizer();

    /**
     * Handle changes from working thread
     *
     * @param changes Changes
     */
    @SuppressWarnings({"unchecked"})
    void handleProgress(T... changes);
}