/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.mp_test.MpTestObj;
import com.haulmont.cuba.core.mp_test.nested.MpTestNestedEnum;
import com.haulmont.cuba.core.mp_test.nested.MpTestNestedObj;
import com.haulmont.cuba.testsupport.TestAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.annotation.Nullable;
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

        appender.getMessages().clear();

        String msg = messages.getMessage(MpTestNestedObj.class, "key0");
        assertEquals("Message0", msg);

        assertEquals(4,
                Iterables.size(Iterables.filter(appender.getMessages(), new Predicate<String>() {
                    @Override
                    public boolean apply(@Nullable String input) {
                        return input != null && input.contains("searchFiles:");
                    }
                }))
        );
        assertEquals(4,
                Iterables.size(Iterables.filter(appender.getMessages(), new Predicate<String>() {
                    @Override
                    public boolean apply(@Nullable String input) {
                        return input != null && input.contains("searchClasspath:");
                    }
                }))
        );

        appender.getMessages().clear();

        msg = messages.getMessage(MpTestNestedObj.class, "key0");
        assertEquals("Message0", msg);

        assertEquals(0,
                getSearchMessagesCount()
        );
    }

    public void testCachingFrenchLoc() {
        Messages messages = prepareCachingTest();

        appender.getMessages().clear();

        String msg = messages.getMessage(MpTestNestedObj.class, "key0", Locale.forLanguageTag("fr"));
        assertEquals("Message0 in French", msg);
        assertEquals(6, getSearchMessagesCount());

        appender.getMessages().clear();

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

        appender.getMessages().clear();

        String msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0");
        assertEquals("Message0", msg);
        assertEquals(14, getSearchMessagesCount());

        appender.getMessages().clear();

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0");
        assertEquals("Message0", msg);
        assertEquals(0, getSearchMessagesCount());
    }

    public void testCachingFrenchLocSeveralPacks() {
        Messages messages = prepareCachingTest();

        appender.getMessages().clear();

        String msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0",
                Locale.forLanguageTag("fr"));
        assertEquals("Message0 in French", msg);
        assertEquals(12, getSearchMessagesCount());

        appender.getMessages().clear();

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0",
                Locale.forLanguageTag("fr"));
        assertEquals("Message0 in French", msg);
        assertEquals(0, getSearchMessagesCount());
    }

    private int getSearchMessagesCount() {
        return Iterables.size(Iterables.filter(appender.getMessages(), new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input != null && (input.contains("searchFiles:") || input.contains("searchClasspath:"));
            }
        }));
    }

}
