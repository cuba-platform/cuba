/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
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

    @ManagedOperation(description = "Removes executions occured earlier than 'age' for tasks with period lesser than 'maxPeriod'")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "age", description = "Execution age in hours"),
            @ManagedOperationParameter(name = "maxPeriod", description = "Max task period in hours")})
    String removeExecutionHistory(String age, String maxPeriod);
}
