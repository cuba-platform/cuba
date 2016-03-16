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

package com.haulmont.cuba.web.gui.executors.impl;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.executors.impl.TaskHandlerImpl;
import com.haulmont.cuba.gui.executors.impl.TasksWatchDog;
import com.haulmont.cuba.web.WebConfig;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 */
@Component(TasksWatchDog.NAME)
public class WebTasksWatchDog extends TasksWatchDog {

    @Inject
    protected Configuration configuration;

    @Override
    protected boolean checkHangup(long actualTimeMs, TaskHandlerImpl taskHandler) {
        WebConfig webConfig = configuration.getConfig(WebConfig.class);

        long timeout = taskHandler.getTimeoutMs();
        long latencyMs = TimeUnit.SECONDS.toMillis(webConfig.getClientBackgroundTasksLatencySeconds());

        // kill tasks, which do not update status for latency milliseconds
        return timeout > 0 && (actualTimeMs - taskHandler.getStartTimeStamp()) > timeout + latencyMs;
    }
}