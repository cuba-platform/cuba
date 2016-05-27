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

package com.haulmont.chile.core.datatypes.impl;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.sys.AppContext;
import mockit.NonStrictExpectations;
import org.junit.Ignore;

import java.util.Locale;

@Ignore
public abstract class AbstractDatatypeTest extends CubaClientTestCase {

    protected Locale ruLocale;
    protected Locale enGbLocale;

    public void setUp() {
        ruLocale = Locale.forLanguageTag("ru");
        enGbLocale = Locale.forLanguageTag("en_GB");

        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        new NonStrictExpectations() {
            {
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.gui";
                globalConfig.getAvailableLocales(); result = ImmutableMap.of("ru", ruLocale, "en_GB", enGbLocale);
            }
        };
        messages.init();
    }
}