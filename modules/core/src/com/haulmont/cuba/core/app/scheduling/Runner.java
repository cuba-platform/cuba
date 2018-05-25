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
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;

/**
 * Interface used by {@link Scheduling} to run scheduled tasks.
 *
 */
public interface Runner {

    String NAME = "cuba_SchedulingRunner";

    void runTask(ScheduledTask task, long now, @Nullable UserSession userSession);

    /**
     * Runs a task right now and only once.
     *
     * @param task        task to execute
     * @param now         current time in milliseconds
     * @param userSession user session
     */
    void runTaskOnce(ScheduledTask task, long now, @Nullable UserSession userSession);
}
