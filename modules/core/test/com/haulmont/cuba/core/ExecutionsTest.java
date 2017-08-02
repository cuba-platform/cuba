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

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.app.execution.ExecutionContextHolder;
import com.haulmont.cuba.core.app.execution.Executions;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestUserSessionSource;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class ExecutionsTest {
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void testExceptionOnUserSession() {
        Executions executions = AppBeans.get(Executions.NAME);
        executions.startExecution("key", "group");
        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
        try {
            ((TestUserSessionSource) userSessionSource).setExceptionOnGetUserSession(true);
        } finally {
            try {
                executions.endExecution();
            } catch (Exception e) {
                //Do nothing
            }
        }
        assertNull(ExecutionContextHolder.getCurrentContext());
        ((TestUserSessionSource) userSessionSource).setExceptionOnGetUserSession(false);
        executions.startExecution("key", "group");
        try {
            executions.endExecution();
        } catch (Exception e) {
            //Do nothing
        }
        assertNull(ExecutionContextHolder.getCurrentContext());
    }
}
