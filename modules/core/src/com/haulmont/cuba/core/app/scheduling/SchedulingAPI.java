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

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.entity.ScheduledTask;

import java.util.List;

/**
 * Interface to control processing of {@link ScheduledTask}s.
 *
 */
public interface SchedulingAPI {

    String NAME = "cuba_Scheduling";

    /**
     * Whether the scheduling is currently active. Default value is defined by
     * {@link com.haulmont.cuba.core.app.ServerConfig#getSchedulingActive()} configuration parameter.
     * @return  true if scheduling is active
     */
    boolean isActive();

    /**
     * Activate/deactivate scheduling. This method affects only the current server run. After restart the
     * state of scheduling is defined by {@link com.haulmont.cuba.core.app.ServerConfig#getSchedulingActive()}
     * configuration parameter.
     * @param value true to activate scheduling
     */
    void setActive(boolean value);

    /**
     * Process all active scheduled tasks once. This method should be invoked from a Spring scheduler, e.g.:
     * <pre>&lt;task:scheduled ref="cuba_Scheduling" method="processScheduledTasks" fixed-rate="1000"/&gt;</pre>
     *
     * <p>This methods returns immediately if scheduling is not active.</p>
     */
    void processScheduledTasks();

    void processScheduledTasks(boolean onlyIfActive);

    /**
     * Mark the sheduled task as running/not running in the internal list. This method should not be used in the
     * application code.
     * @param task      task instance
     * @param running   true to mark as running, false to mark as not running
     */
    void setRunning(ScheduledTask task, boolean running);

    /**
     * @return a list of active task instances in detached state
     */
    List<ScheduledTask> getActiveTasks();
}
