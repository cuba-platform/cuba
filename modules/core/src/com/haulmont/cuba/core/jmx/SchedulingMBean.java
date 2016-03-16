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

package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.scheduling.SchedulingAPI}.
 *
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
