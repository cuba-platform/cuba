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

import java.util.UUID;

public interface Executions {
    String NAME = "cuba_Executions";

    /**
     * INTERNAL
     * Start execution context for the current thread.
     * Bind execution context to the current thread and user session
     * @param key - unique identifier of execution
     * @param group - group is used for grouping execution by type.
     *              E.g. group 'Reporting' is used for all reporting
     *              module executions
     * @return created execution context
     */
    ExecutionContext startExecution(String key, String group);

    /**
     * INTERNAL
     * End execution context for the current thread.
     * Unbind from user session and thread
     */
    void endExecution();

    /**
     * Cancel execution context:
     *  - cancel all resources with context. E.g. for JDBC cancel executed statements.
     *  - interrupt thread of execution context.
     * @param userSessionId - user session in that started execution context
     * @param group - group of started execution context
     * @param key - key of started execution context
     */
    void cancelExecution(UUID userSessionId, String group, String key);

    /**
     * INTERNAL
     * @return execution context that is bound to the current thread
     */
    ExecutionContext getCurrentContext();
}
