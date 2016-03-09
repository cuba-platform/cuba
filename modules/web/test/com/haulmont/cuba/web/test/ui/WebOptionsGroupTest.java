/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.test.ui;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.OptionsGroupTest;
import com.haulmont.cuba.web.gui.WebComponentsFactory;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.server.VaadinSession;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import java.util.Locale;

/**
 * @author petunin
 */
public class WebOptionsGroupTest extends OptionsGroupTest {
    @Mocked
    VaadinSession vaadinSession;

    public WebOptionsGroupTest() {
        factory = new WebComponentsFactory();
    }

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new NonStrictExpectations() {
            {
                vaadinSession.getLocale(); result = Locale.ENGLISH;
                VaadinSession.getCurrent(); result = vaadinSession;

                vaadinSession.getConverterFactory(); result = new DefaultConverterFactory();

                globalConfig.getAvailableLocales(); result = ImmutableMap.of("en", Locale.ENGLISH);
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.web";
            }
        };
    }
}
