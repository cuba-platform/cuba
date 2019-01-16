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

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ds.api.consistency.LookupPickerFieldDsTest;
import com.haulmont.cuba.web.gui.components.WebLookupPickerField;
import com.haulmont.cuba.web.test.stubs.TestUiComponents;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.data.util.converter.DefaultConverterFactory;
import mockit.Expectations;
import mockit.Mocked;

public class WebLookupPickerFieldDsTest extends LookupPickerFieldDsTest {

    @Mocked
    protected VaadinSession vaadinSession;
    @Mocked
    protected ClientConfig clientConfig;

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new Expectations() {
            {
                VaadinSession.getCurrent(); result = vaadinSession; minTimes = 0;
                vaadinSession.getConverterFactory(); result = new DefaultConverterFactory(); minTimes = 0;
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.web"; minTimes = 0;
                configuration.getConfig(ClientConfig.class); result = clientConfig; minTimes = 0;
                clientConfig.getPickerShortcutModifiers(); result = "CTRL-ALT"; minTimes = 0;
            }
        };
        this.uiComponents = new TestUiComponents(applicationContext);
    }

    @Override
    protected void autowireUiComponent(Component component) {
        super.autowireUiComponent(component);

        WebLookupPickerField lookupPickerField = (WebLookupPickerField) component;
        lookupPickerField.setBeanLocator(beanLocator);
        lookupPickerField.setMetadataTools(metadata.getTools());
    }
}
