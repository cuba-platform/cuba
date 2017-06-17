/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.core.app.execution;

import java.util.Date;

/**
 * INTERNAL
 * Object represents execution and
 * all resources associated with the execution
 */
public interface ExecutionContext {
    /**
     * ACTIVE - execution is started
     * COMPLETED - execution is completed
     * CANCELED - execution is canceled by user request
     */
    enum State {
        ACTIVE,
        COMPLETED,
        CANCELED
    }

    /**
     * @return key - unique identifier of execution
     */
    String getKey();

    /**
     * @return group which is used for grouping execution by type.
     * E.g. group 'Reporting' is used for all reporting
     * module executions
     */
    String getGroup();

    /**
     * @return execution start time
     */
    Date getStartTime();

    /**
     * @return state - execution state
     */
    State getState();

    /**
     * @return true if execution in CANCELED state
     */
    boolean isCanceled();

    /**
     * add cancelable resource to the current execution
     */
    void addResource(CancelableResource resource);

    /**
     * remove cancelable resource to the current execution
     */
    void removeResource(CancelableResource resource);
}
