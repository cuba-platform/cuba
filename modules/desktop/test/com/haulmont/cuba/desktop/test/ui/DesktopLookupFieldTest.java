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

package com.haulmont.cuba.desktop.test.ui;

import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.gui.DesktopComponentsFactory;
import com.haulmont.cuba.gui.components.LookupFieldTest;
import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrictExpectations;

import java.util.Locale;

/**
 */
public class DesktopLookupFieldTest extends LookupFieldTest {

    public DesktopLookupFieldTest() {
        factory = new DesktopComponentsFactory();
    }

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new NonStrictExpectations() {
            {
                globalConfig.getAvailableLocales(); result = ImmutableMap.of("en", Locale.ENGLISH);
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.desktop";
            }
        };

        new MockUp<AutoCompleteSupport>() {
            @SuppressWarnings("UnusedDeclaration")
            @Mock
            public void checkAccessThread() {
            }
        };
    }
}