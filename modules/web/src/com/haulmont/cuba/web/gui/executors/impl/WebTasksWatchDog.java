/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.executors.impl;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.executors.impl.TaskHandlerImpl;
import com.haulmont.cuba.gui.executors.impl.TasksWatchDog;
import com.haulmont.cuba.web.WebConfig;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(TasksWatchDog.NAME)
public class WebTasksWatchDog extends TasksWatchDog {

    @Inject
    protected Configuration configuration;

    @Override
    protected boolean checkHangup(long actualTimeMs, TaskHandlerImpl taskHandler) {
        WebConfig webConfig = configuration.getConfig(WebConfig.class);

        long timeout = taskHandler.getStartTimeStamp();
        long latencyMs = TimeUnit.SECONDS.toMillis(webConfig.getClientBackgroundTasksLatencySeconds());

        // kill tasks, which do not update status for latency milliseconds
        return timeout > 0 && (actualTimeMs - taskHandler.getStartTimeStamp()) > timeout + latencyMs;
    }
}