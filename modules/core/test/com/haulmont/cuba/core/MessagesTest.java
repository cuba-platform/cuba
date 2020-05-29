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
package com.haulmont.cuba.core;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.mp_test.MpTestObj;
import com.haulmont.cuba.core.mp_test.nested.MpTestNestedEnum;
import com.haulmont.cuba.core.mp_test.nested.MpTestNestedObj;
import com.haulmont.cuba.testsupport.TestAppender;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestMessageTools;
import org.apache.commons.lang3.LocaleUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MessagesTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private final TestAppender appender;

    public MessagesTest() {
        appender = new TestAppender();
        appender.start();

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger("com.haulmont.cuba.core.sys.AbstractMessages");
        logger.addAppender(appender);
        logger.setLevel(Level.TRACE);
    }

    @Test
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

    @Test
    public void testInclude() {
        Messages messages = AppBeans.get(Messages.class);

        String msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "includedMsg");
        assertEquals("Included Message", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "includedMsgToOverride");
        assertEquals("Overridden Included Message", msg);
    }

    @Test
    public void testMultiInclude() {
        Messages messages = AppBeans.get(Messages.class);

        String msg1 = messages.getMessage("com.haulmont.cuba.core.mp_test.includes", "oneKey");
        assertEquals(msg1, "OK");

        String msg2 = messages.getMessage("com.haulmont.cuba.core.mp_test.includes", "twoKey");
        assertEquals(msg2, "OK");

        String msg3 = messages.getMessage("com.haulmont.cuba.core.mp_test.includes", "threeKey");
        assertEquals(msg3, "overridden");
    }

    @Test
    public void testIncludeDefaultLoc() {
        Messages messages = AppBeans.get(Messages.class);
        TestMessageTools messageTools = (TestMessageTools) AppBeans.get(MessageTools.class);
        try {
            Locale localeRu = Locale.forLanguageTag("ru");
            String msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "includedMsg", localeRu);
            assertEquals("Included Message", msg);

            messageTools.setDefaultLocale(localeRu);
            messages.clearCache();
            msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "includedMsg", localeRu);
            assertEquals("Included Message RU", msg);
        } finally {
            messageTools.setDefaultLocale(null);
            messages.clearCache();
        }
    }

    @Test
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

    @Test
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
        return messages;
    }

    @Test
    public void testCachingDefaultLocSeveralPacks() {
        Messages messages = prepareCachingTest();

        appender.getMessages().clear();

        String msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0");
        assertEquals("Message0", msg);
        assertEquals(16, getSearchMessagesCount());

        appender.getMessages().clear();

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0");
        assertEquals("Message0", msg);
        assertEquals(0, getSearchMessagesCount());
    }

    @Test
    public void testCachingFrenchLocSeveralPacks() {
        Messages messages = prepareCachingTest();

        appender.getMessages().clear();

        String msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0",
                Locale.forLanguageTag("fr"));
        assertEquals("Message0 in French", msg);
        assertEquals(14, getSearchMessagesCount());

        appender.getMessages().clear();

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested com.haulmont.cuba.core.mp_test", "key0",
                Locale.forLanguageTag("fr"));
        assertEquals("Message0 in French", msg);
        assertEquals(0, getSearchMessagesCount());
    }

    @Test
    public void testFind() throws Exception {
        Messages messages = AppBeans.get(Messages.class);
        UserSessionSource uss = AppBeans.get(UserSessionSource.class);

        String msg = messages.findMessage("com.haulmont.cuba.core.mp_test.nested", "key0", uss.getLocale());
        assertEquals("Message0", msg);

        msg = messages.findMessage("com.haulmont.cuba.core.mp_test.nested", "non-existing-message", uss.getLocale());
        assertNull(msg);

        msg = messages.findMessage("com.haulmont.cuba.core.mp_test.nested", "key0", null);
        assertEquals("Message0", msg);

        msg = messages.findMessage("com.haulmont.cuba.core.mp_test.nested", "non-existing-message", null);
        assertNull(msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test.nested", "non-existing-message", uss.getLocale());
        assertEquals("non-existing-message", msg);
    }

    @Test
    public void testMainMessagePack() throws Exception {
        Messages messages = AppBeans.get(Messages.class);

        String msg;

        msg = messages.getMainMessage("trueString", Locale.forLanguageTag("en"));
        assertEquals("True", msg);

        msg = messages.getMessage("com.haulmont.cuba.something", "trueString", Locale.forLanguageTag("en"));
        assertEquals("True", msg);

        appender.getMessages().clear();
        msg = messages.getMessage("com.haulmont.cuba.something", "trueString", Locale.forLanguageTag("en"));
        assertEquals("True", msg);
        assertEquals(0, getSearchMessagesCount());
    }

    /**
     * Test hierarchy of country/language/default message packs.
     * <p>
     * messages.properties:<br>
     * commonMsg=Common Message<br>
     * languageMsg=Language Message<br>
     * countryMsg=Country Message<br>
     * <p>
     * messages_fr.properties:<br>
     * languageMsg=Language Message fr<br>
     * countryMsg=Country Message fr<br>
     * <p>
     * messages_fr_CA.properties:<br>
     * countryMsg=Country Message fr CA<br>
     */
    @Test
    public void testLanguageAndCountry() throws Exception {
        Messages messages = AppBeans.get(Messages.class);

        Map<String, Locale> availableLocales = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class).getAvailableLocales();
        assertTrue(availableLocales.containsValue(Locale.forLanguageTag("fr")));
        assertTrue(availableLocales.containsValue(Locale.forLanguageTag("fr-CA")));

        boolean localeLanguageOnly = messages.getTools().useLocaleLanguageOnly();
        assertFalse(localeLanguageOnly);

        String msg;

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "commonMsg", Locale.forLanguageTag("fr-CA"));
        assertEquals("Common Message", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "languageMsg", Locale.forLanguageTag("fr-CA"));
        assertEquals("Language Message fr", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "countryMsg", Locale.forLanguageTag("fr-CA"));
        assertEquals("Country Message fr CA", msg);
    }

    @Test
    public void testScriptAndVariant() throws Exception {
        Messages messages = AppBeans.get(Messages.class);

        Map<String, Locale> availableLocales = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class).getAvailableLocales();
        assertTrue(availableLocales.containsValue(LocaleUtils.toLocale("sr")));
        assertTrue(availableLocales.containsValue(Locale.forLanguageTag("sr-Latn")));
        assertTrue(availableLocales.containsValue(LocaleUtils.toLocale("ja")));
        assertTrue(availableLocales.containsValue(LocaleUtils.toLocale("ja_JP_JP")));

        assertEquals(LocaleResolver.resolve("sr-Latn"), Locale.forLanguageTag("sr-Latn"));
        assertEquals(LocaleResolver.resolve("ja_JP_JP"), LocaleUtils.toLocale("ja_JP_JP"));

        boolean localeLanguageOnly = messages.getTools().useLocaleLanguageOnly();
        assertFalse(localeLanguageOnly);

        String msg;

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "fullMsg", Locale.forLanguageTag("sr-Latn"));
        assertEquals("Full Message sr-Latn", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "languageMsg", LocaleUtils.toLocale("sr"));
        assertEquals("Language Message sr", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "languageMsg", Locale.forLanguageTag("sr-Latn"));
        assertEquals("Language Message sr", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "fullMsg", LocaleUtils.toLocale("ja_JP_JP"));
        assertEquals("Full Message ja_JP_JP", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "languageMsg", LocaleUtils.toLocale("ja"));
        assertEquals("Language Message ja", msg);

        msg = messages.getMessage("com.haulmont.cuba.core.mp_test", "languageMsg", LocaleUtils.toLocale("ja_JP_JP"));
        assertEquals("Language Message ja", msg);
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
