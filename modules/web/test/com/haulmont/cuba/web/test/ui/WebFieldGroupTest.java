/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.test.ui;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.FieldGroupTest;
import com.haulmont.cuba.web.gui.WebUiComponents;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.data.util.converter.DefaultConverterFactory;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Ignore;

import java.util.Locale;

@Ignore
public class WebFieldGroupTest extends FieldGroupTest {
    @Mocked
    protected VaadinSession vaadinSession;

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new Expectations() {
            {
                vaadinSession.getLocale(); result = Locale.ENGLISH; minTimes = 0;
                VaadinSession.getCurrent(); result = vaadinSession; minTimes = 0;

                vaadinSession.getConverterFactory(); result = new DefaultConverterFactory(); minTimes = 0;

                globalConfig.getAvailableLocales(); result = ImmutableMap.of("en", Locale.ENGLISH); minTimes = 0;
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.web"; minTimes = 0;
            }
        };
    }

    @Override
    protected UiComponents createComponentsFactory() {
        return new WebUiComponents();
    }

    @Override
    protected int getGridRows(FieldGroup fieldGroup) {
        return fieldGroup.unwrap(GridLayout.class).getRows();
    }

    @Override
    protected int getGridColumns(FieldGroup fieldGroup) {
        return fieldGroup.unwrap(GridLayout.class).getColumns();
    }

    @Override
    protected Object getGridCellComposition(FieldGroup fieldGroup, int col, int row) {
        return fieldGroup.unwrap(GridLayout.class).getComponent(col, row);
    }
}