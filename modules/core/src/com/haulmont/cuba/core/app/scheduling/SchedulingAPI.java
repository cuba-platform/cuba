/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.entity.ScheduledTask;

/**
 * Interface to control processing of {@link ScheduledTask}s.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 * @see Scheduling
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

    /**
     * Mark the sheduled task as running/not running in the internal list. This method should not be used in the
     * application code.
     * @param task      task instance
     * @param running   true to mark as running, false to mark as not running
     */
    void setRunning(ScheduledTask task, boolean running);
}
