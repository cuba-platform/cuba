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

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.app.scheduled.MethodInfo;
import com.haulmont.cuba.core.entity.ScheduledTask;

import java.util.List;
import java.util.Map;

/**
 * Service interface to control {@link com.haulmont.cuba.core.entity.ScheduledTask}s configuration and execution.
 *
 *
 */
public interface SchedulingService {

    String NAME = "cuba_ScheduledTasksService";

    /**
     * Return information about beans and their methods that can be invoked by scheduled tasks.
     * @return  map of bean names to lists of their methods
     */
    Map<String, List<MethodInfo>> getAvailableBeans();

    /**
     * Return a list of user login names that can be used by scheduled tasks.
     * @return  list of user login names
     */
    List<String> getAvailableUsers();

    /**
     * Activate/deactivate scheduling for all servers in the cluster.
     *
     * <p>This method affects only the current run of each server. After server restart the
     * state of scheduling is defined by <code>ServerConfig#getSchedulingActive()</code> configuration parameter.</p>
     *
     * @param active    true to activate scheduling
     */
    void setActive(boolean active);

    /**
     * Activate/deactivate specific task.
     * @param task      task instance
     * @param active    true to activate
     */
    void setActive(ScheduledTask task, boolean active);
}
