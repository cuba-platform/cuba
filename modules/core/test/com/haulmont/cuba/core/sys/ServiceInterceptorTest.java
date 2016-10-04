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

package com.haulmont.cuba.core.sys;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.TestingService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.security.app.UserSessions;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.testsupport.TestAppender;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceInterceptorTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private final TestAppender appender;

    public ServiceInterceptorTest() {
        appender = new TestAppender();
        appender.start();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger("com.haulmont.cuba.core.sys.ServiceInterceptor");
        logger.addAppender(appender);

//        Logger.getRootLogger().addAppender(appender);
    }

    @Test
    public void testOpenTransaction() throws Exception {
        TestingService service = AppBeans.get(TestingService.class);
        appender.getMessages().clear();

        // programmatic tx without proper completion
        Object tx = service.leaveOpenTransaction();
        ((Transaction) tx).commit();
        assertEquals(1, appender.getMessages().stream().filter(s -> s.contains("Open transaction")).count());

        appender.getMessages().clear();

        // declarative tx
        service.declarativeTransaction();
        assertEquals(0, appender.getMessages().stream().filter(s -> s.contains("Open transaction")).count());
    }

    @Test
    public void testLogMessage() throws Exception {
        TestingService service1 = AppBeans.get(TestingService.class);
        ServiceInterceptorTestService service2 = AppBeans.get(ServiceInterceptorTestService.class);

        // normally no log messages for internal invocations
        appender.getMessages().clear();
        service1.declarativeTransaction();
        assertEquals(0, appender.getMessages().stream().filter(s -> s.contains("from another service")).count());

        appender.getMessages().clear();
        service2.declarativeTransaction();
        assertEquals(0, appender.getMessages().stream().filter(s -> s.contains("from another service")).count());

        // old behaviour
        ServiceInterceptor serviceInterceptor = AppBeans.get(ServiceInterceptor.class);
        serviceInterceptor.logInternalServiceInvocation = true;
        try {
            appender.getMessages().clear();
            service1.declarativeTransaction();
            assertEquals(0, appender.getMessages().stream().filter(s -> s.contains("from another service")).count());

            appender.getMessages().clear();
            service2.declarativeTransaction();
            assertEquals(1, appender.getMessages().stream().filter(s -> s.contains("from another service")).count());
        } finally {
            serviceInterceptor.logInternalServiceInvocation = false;
        }
    }

    @Test
    public void testExceptionHandling() throws Exception {
        TestingService service1 = AppBeans.get(TestingService.class);
        ServiceInterceptorTestService service2 = AppBeans.get(ServiceInterceptorTestService.class);

        appender.getMessages().clear();
        try {
            service1.executeWithException();
        } catch (Exception e) {
            assertTrue(e instanceof RemoteException
                    && ((RemoteException) e).getFirstCauseException() instanceof TestingService.TestException);
        }

        appender.getMessages().clear();
        try {
            service2.executeWithException();
        } catch (Exception e) {
            assertTrue(e instanceof RemoteException
                    && ((RemoteException) e).getFirstCauseException() instanceof TestingService.TestException);
        }
    }

    @Test
    public void testNewThread() throws Exception {
        ServiceInterceptorTestService service = AppBeans.get(ServiceInterceptorTestService.class);
        UserSessions userSessions = AppBeans.get(UserSessions.class);

        // workaround for test security setup
        Field startedField = AppContext.class.getDeclaredField("started");
        startedField.setAccessible(true);
        startedField.set(null, true);
        AppContext.setSecurityContext(AppContext.NO_USER_CONTEXT);
        UserSession userSession = new UserSession(AppContext.NO_USER_CONTEXT.getSessionId(), new User(), Collections.emptyList(), Locale.ENGLISH, true);
        userSessions.add(userSession);

        try {
            appender.getMessages().clear();
            service.declarativeTransactionNewThread();
            assertEquals(0, appender.getMessages().stream().filter(s -> s.contains("from another service")).count());

            appender.getMessages().clear();
            try {
                service.executeWithExceptionNewThread();
            } catch (Exception e) {
                assertTrue(e instanceof RemoteException
                        && ((RemoteException) e).getFirstCauseException() instanceof TestingService.TestException);
            }

        } finally {
            userSessions.remove(userSession);
            startedField.set(null, false);
        }
    }
}