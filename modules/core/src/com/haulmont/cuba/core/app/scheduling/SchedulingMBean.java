/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.scheduling;

/**
 * JMX interface to control processing of {@link com.haulmont.cuba.core.entity.ScheduledTask}s.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface SchedulingMBean {

    /**
     * @return  true if scheduling is active
     * @see     com.haulmont.cuba.core.app.scheduling.SchedulingAPI#isActive()
     */
    boolean isActive();

    /**
     * @param value true to activate scheduling
     * @see         com.haulmont.cuba.core.app.scheduling.SchedulingAPI#setActive(boolean)
     */
    void setActive(boolean value);

    /**
     * @return  a string representation of active scheduled tasks list
     */
    String printActiveScheduledTasks();

    /**
     * This method is for testing purposes, it starts the processing once, regardless of "active" attribute.
     * @return  a message about succesful execution or a stacktrace in case of error
     */
    String processScheduledTasksOnce();
}
