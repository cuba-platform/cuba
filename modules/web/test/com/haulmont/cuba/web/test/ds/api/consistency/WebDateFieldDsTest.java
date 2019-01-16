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
 */

package com.haulmont.cuba.web.test.ds.api.consistency;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ds.api.consistency.DateFieldDsTest;
import com.haulmont.cuba.web.gui.components.WebDateField;
import com.haulmont.cuba.web.test.stubs.TestUiComponents;
import mockit.Expectations;

import java.util.Locale;

public class WebDateFieldDsTest extends DateFieldDsTest {

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new Expectations() {
            {
                globalConfig.getAvailableLocales(); result = ImmutableMap.of("en", Locale.ENGLISH); minTimes = 0;
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.web"; minTimes = 0;
            }
        };

        uiComponents = new TestUiComponents(applicationContext);
    }

    @Override
    protected void autowireUiComponent(Component component) {
        super.autowireUiComponent(component);

        WebDateField dateField = (WebDateField) component;
        dateField.setBeanLocator(beanLocator);
        dateField.setDateTimeTransformations(new DateTimeTransformations());
    }
}