/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.scheduling.SchedulingAPI}.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedResource(description = "Controls execution of scheduled tasks")
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

    @ManagedOperation(description = "Print active scheduled tasks")
    String printActiveScheduledTasks();

    @ManagedOperation(description = "Starts the processing once, regardless of 'active' attribute")
    String processScheduledTasks();
}
