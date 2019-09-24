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

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.gui.components.data.value.ValueBinder;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.Locale;

public class AbstractComponentTestCase extends CubaClientTestCase {

    protected ComponentsFactory factory;

    protected UiComponents uiComponents;

    @Mocked
    protected BackgroundWorker backgroundWorker;
    @Mocked
    protected ApplicationContext applicationContext;
    @Mocked
    protected AutowireCapableBeanFactory beanFactory;
    @Mocked
    protected BeanLocator beanLocator;
    @Mocked
    protected UserSessionSource userSessionSource;

    protected ValueBinder valueBinder;
    protected OptionsBinder optionsBinder;

    @BeforeEach
    public void setUp() {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();
        setupGuiInfrastructure();

        initExpectations();

        messages.init();
    }

    protected void setupGuiInfrastructure() {
        this.valueBinder = new TestValueBinder(beanLocator, messageTools, metadata.getTools(), beanValidation, security);
        this.optionsBinder = new OptionsBinder();

        new Expectations() {
            {
                AppBeans.get(BackgroundWorker.NAME); result = backgroundWorker; minTimes = 0;
                AppBeans.get(BackgroundWorker.class); result = backgroundWorker; minTimes = 0;
                AppBeans.get(BackgroundWorker.NAME, BackgroundWorker.class); result = backgroundWorker; minTimes = 0;

                applicationContext.getAutowireCapableBeanFactory(); result = beanFactory; minTimes = 0;

                beanFactory.autowireBean(any); result = new Delegate() {
                    @SuppressWarnings("unused")
                    void autowireBean(java.lang.Object o) throws org.springframework.beans.BeansException {
                        autowireUiComponent((Component) o);
                    }
                }; minTimes = 0;

                userSessionSource.getLocale(); result = Locale.ENGLISH; minTimes = 0;

                beanLocator.get(MetadataTools.NAME); result = metadata.getTools(); minTimes = 0;

                beanLocator.get(ValueBinder.NAME); result = valueBinder; minTimes = 0;
                beanLocator.get(ValueBinder.class); result = valueBinder; minTimes = 0;
                beanLocator.get(ValueBinder.NAME, ValueBinder.class); result = valueBinder; minTimes = 0;

                beanLocator.get(OptionsBinder.NAME); result = optionsBinder; minTimes = 0;
                beanLocator.get(OptionsBinder.NAME, OptionsBinder.class); result = optionsBinder; minTimes = 0;

                beanLocator.get(Configuration.NAME); result = configuration; minTimes = 0;
                beanLocator.get(FormatStringsRegistry.NAME); result = formatStringsRegistry; minTimes = 0;
            }
        };
    }

    protected void initExpectations() {
    }

    protected void autowireUiComponent(Component component) {

    }

    public static class TestValueBinder extends ValueBinder {
        @SuppressWarnings("ReassignmentInjectVariable")
        public TestValueBinder(BeanLocator beanLocator, MessageTools messageTools,
                               MetadataTools metadataTools, BeanValidation beanValidation,
                               Security security) {
            this.beanLocator = beanLocator;
            this.messageTools = messageTools;
            this.metadataTools = metadataTools;
            this.beanValidation = beanValidation;
            this.security = security;
        }
    }
}