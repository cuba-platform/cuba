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

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Lifecycle object that is passed to {@link BackgroundTask#run(TaskLifeCycle)} method to allow working thread to
 * interact with the execution environment.
 *
 * @param <T> task progress measurement unit
 *
 */
public interface TaskLifeCycle<T> {

    /**
     * Publish changes to show progress.
     *
     * @param changes Changes
     */
    @SuppressWarnings({"unchecked"})
    void publish(T... changes);

    /**
     * @return true if the working thread has been interrupted
     */
    boolean isInterrupted();

    /**
     * @return execution parameters that was set by {@link BackgroundTask#getParams()}
     */
    @Nonnull
    Map<String, Object> getParams();
}