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

package com.haulmont.cuba.web.test.ui;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.LookupFieldTest;
import com.haulmont.cuba.web.gui.WebComponentsFactory;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.server.VaadinSession;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import java.util.Locale;

/**
 */
public class WebLookupFieldTest extends LookupFieldTest {

    @Mocked
    VaadinSession vaadinSession;

    public WebLookupFieldTest() {
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