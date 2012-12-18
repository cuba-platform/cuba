/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 06.03.2009 12:30:48
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.mp_test.MpTestObj;
import com.haulmont.cuba.core.mp_test.nested.MpTestNestedEnum;
import com.haulmont.cuba.core.mp_test.nested.MpTestNestedObj;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MessageProviderTest extends CubaTestCase {

    private final TestAppender appender;

    public MessageProviderTest() {
        appender = new TestAppender();
        Logger.getRootLogger().addAppender(appender);
    }

    public void test() {
        Messages messages = AppBeans.get(Messages.class);

        String msg = messages.getMessage(MpTestNestedObj.class, "key0");
        assertEquals("Message0", msg);

        msg = messages.getMessage(MpTestObj.class, "key1");
        assertEquals("Message1", msg);

        msg = messages.getMessage(MpTestNestedObj.class, "key2");
        assertEquals("Message2", msg);

        // test cache
        msg = messages.getMessage(MpTestNestedObj.class, "key0");
        assertEquals("Message0", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested", "key1");
        assertEquals("Message1", msg);

        msg = messages.getMessage("test", "key1");
        assertEquals("key1", msg);

        msg = messages.getMessage(MpTestNestedEnum.ONE);
        assertEquals("One", msg);

        msg = messages.getMessage(MpTestNestedObj.InternalEnum.FIRST);
        assertEquals("First", msg);

    }

    public void testInclude() {
        Messages messages = AppBeans.get(Messages.class);

        String msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "includedMsg");
        assertEquals("Included Message", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "includedMsgToOverride");
        assertEquals("Overridden Included Message", msg);
    }

    public void testMultiInclude() {
        Messages messages = AppBeans.get(Messages.class);

        String msg1 = messages.getMessage("com.haulmont.cuba.core.mp_test.includes", "oneKey");
        assertEquals(msg1, "OK");

        String msg2 = messages.getMessage("com.haulmont.cuba.core.mp_test.includes", "twoKey");
        assertEquals(msg2, "OK");

        String msg3 = messages.getMessage("com.haulmont.cuba.core.mp_test.includes", "threeKey");
        assertEquals(msg3, "overridden");
    }

    public void testCachingDefaultLoc() {
        Messages messages = prepareCachingTest();

        appender.messages.clear();

        String msg = messages.getMessage(MpTestNestedObj.class, "key0");
        assertEquals("Message0", msg);

        assertEquals(2,
                Iterables.size(Iterables.filter(appender.messages, new Predicate<String>() {
                    @Override
                    public boolean apply(@Nullable String input) {
                        return input != null && input.contains("searchFiles:");
                    }
                }))
        );
        assertEquals(2,
                Iterables.size(Iterables.filter(appender.messages, new Predicate<String>() {
                    @Override
                    public boolean apply(@Nullable String input) {
                        return input != null && input.contains("searchClasspath:");
                    }
                }))
        );

        appender.messages.clear();

        msg = messages.getMessage(MpTestNestedObj.class, "key0");
        assertEquals("Message0", msg);

        assertEquals(0,
                getSearchMessagesCount()
        );
    }

    public void testCachingFrenchLoc() {
        Messages messages = prepareCachingTest();

        appender.messages.clear();

        String msg = messages.getMessage(MpTestNestedObj.class, "key0", Locale.forLanguageTag("fr"));
        assertEquals("Message0 in French", msg);
        assertEquals(2, getSearchMessagesCount());

        appender.messages.clear();

        msg = messages.getMessage(MpTestNestedObj.class, "key0", Locale.forLanguageTag("fr"));
        assertEquals("Message0 in French", msg);
        assertEquals(0, getSearchMessagesCount());
    }

    private Messages prepareCachingTest() {
        Messages messages = AppBeans.get(Messages.class);
        messages.clearCache();

        Logger logger = Logger.getLogger(messages.getClass());
        logger.setLevel(Level.TRACE);
        return messages;
    }

    public void testCachingDefaultLocSeveralPacks() {
        Messages messages = prepareCachingTest();

        appender.messages.clear();

        String msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0");
        assertEquals("Message0", msg);
        assertEquals(8, getSearchMessagesCount());

        appender.messages.clear();

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0");
        assertEquals("Message0", msg);
        assertEquals(0, getSearchMessagesCount());
    }

    public void testCachingFrenchLocSeveralPacks() {
        Messages messages = prepareCachingTest();

        appender.messages.clear();

        String msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0",
                Locale.forLanguageTag("fr"));
        assertEquals("Message0 in French", msg);
        assertEquals(4, getSearchMessagesCount());

        appender.messages.clear();

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0",
                Locale.forLanguageTag("fr"));
        assertEquals("Message0 in French", msg);
        assertEquals(0, getSearchMessagesCount());
    }

    private int getSearchMessagesCount() {
        return Iterables.size(Iterables.filter(appender.messages, new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input != null && (input.contains("searchFiles:") || input.contains("searchClasspath:"));
            }
        }));
    }

    private static class TestAppender implements Appender {

        private List<String> messages = Collections.synchronizedList(new ArrayList<String>());

        @Override
        public void addFilter(Filter newFilter) {
        }

        @Override
        public Filter getFilter() {
            return null;
        }

        @Override
        public void clearFilters() {
        }

        @Override
        public void close() {
        }

        @Override
        public void doAppend(LoggingEvent event) {
            messages.add(event.getMessage() == null ? "" : event.getMessage().toString());
        }

        @Override
        public String getName() {
            return "TestAppender";
        }

        @Override
        public void setErrorHandler(ErrorHandler errorHandler) {
        }

        @Override
        public ErrorHandler getErrorHandler() {
            return null;
        }

        @Override
        public void setLayout(Layout layout) {
        }

        @Override
        public Layout getLayout() {
            return null;
        }

        @Override
        public void setName(String name) {
        }

        @Override
        public boolean requiresLayout() {
            return false;
        }
    }
}
