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
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.TestingService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.testsupport.TestAppender;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

import static org.junit.Assert.assertEquals;

/**
 */
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
        int size;

        appender.getMessages().clear();

        // programmatic tx without proper completion
        Object tx = service.leaveOpenTransaction();
        ((Transaction) tx).commit();

        size = Iterables.size(Iterables.filter(appender.getMessages(), new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input != null && input.contains("Open transaction");
            }
        }));
        assertEquals(1, size);

        appender.getMessages().clear();

        // declarative tx
        service.declarativeTransaction();

        size = Iterables.size(Iterables.filter(appender.getMessages(), new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input != null && input.contains("Open transaction");
            }
        }));
        assertEquals(0, size);
    }
}
