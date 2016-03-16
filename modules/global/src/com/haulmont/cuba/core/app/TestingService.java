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

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.Logging;
import com.haulmont.cuba.core.global.SupportedByClient;

/**
 * Service interface for integration testing. Don't use it in application code!
 *
 */
public interface TestingService {

    String NAME = "cuba_TestingService";

    String executeFor(int timeMillis);

    // Works in unit test mode only
    String executeUpdateSql(String sql);

    // Works in unit test mode only
    String executeSelectSql(String sql);

    String execute();

    boolean primitiveParameters(boolean b, int i, long l, double d);

    String executeWithException() throws TestException;

    /**
     * Warning! Removes all scheduled tasks from the database!
     */
    void clearScheduledTasks();

    Object leaveOpenTransaction();

    void declarativeTransaction();

    @SupportedByClient
    @Logging(Logging.Type.BRIEF)
    class TestException extends Exception {

        public TestException(String message) {
            super(message);
        }
    }
}