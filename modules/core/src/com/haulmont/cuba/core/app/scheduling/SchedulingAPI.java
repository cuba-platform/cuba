/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.entity.ScheduledTask;

import java.util.List;

/**
 * Interface to control processing of {@link ScheduledTask}s.
 *
 * @author krivopustov
 * @version $Id$
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
