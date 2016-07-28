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

import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;

public abstract class AbstractComponentTest extends CubaClientTestCase {

    protected ComponentsFactory factory;

    @Mocked
    protected BackgroundWorker backgroundWorker;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();
        setupGuiInfrastructure();

        initExpectations();

        messages.init();
    }

    protected void setupGuiInfrastructure() {
        new NonStrictExpectations() {
            {
                AppBeans.get(BackgroundWorker.NAME); result = backgroundWorker;
                AppBeans.get(BackgroundWorker.class); result = backgroundWorker;
                AppBeans.get(BackgroundWorker.NAME, BackgroundWorker.class); result = backgroundWorker;
            }
        };
    }

    protected void initExpectations() {
    }
}