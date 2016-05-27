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

package com.haulmont.cuba.gui.app.core.appproperties;

import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.config.AppPropertyEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AppPropertiesDatasourceTest extends CubaClientTestCase {

    @Before
    public void setUp() throws Exception {
        setupInfrastructure();
    }

    @Test
    public void testTree() throws Exception {
        AppPropertiesDatasource datasource = new AppPropertiesDatasource();

        AppPropertyEntity e1 = new AppPropertyEntity();
        e1.setName("cuba.email.smtpHost");

        AppPropertyEntity e2 = new AppPropertyEntity();
        e2.setName("cuba.email.smtpPort");

        AppPropertyEntity e3 = new AppPropertyEntity();
        e3.setName("cuba.someProp");

        AppPropertyEntity e4 = new AppPropertyEntity();
        e4.setName("someOtherProp");

        List<AppPropertyEntity> list = datasource.createEntitiesTree(Arrays.asList(e1, e2, e3, e4));

        AppPropertyEntity cuba = list.stream().filter(e -> e.getName().equals("cuba")).findFirst().get();
        assertNull(cuba.getParent());

        AppPropertyEntity cubaEmail = list.stream().filter(e -> e.getName().equals("email")).findFirst().get();
        assertTrue(cubaEmail.getParent().equals(cuba));

        AppPropertyEntity cubaEmailSmtpHost = list.stream().filter(e -> e.getName().equals("cuba.email.smtpHost")).findFirst().get();
        assertTrue(cubaEmailSmtpHost.getParent() == cubaEmail);

        AppPropertyEntity cubaEmailSmtpPort = list.stream().filter(e -> e.getName().equals("cuba.email.smtpPort")).findFirst().get();
        assertTrue(cubaEmailSmtpPort.getParent() == cubaEmail);

        AppPropertyEntity cubaSomeProp = list.stream().filter(e -> e.getName().equals("cuba.someProp")).findFirst().get();
        assertTrue(cubaSomeProp.getParent() == cuba);

        AppPropertyEntity someOtherProp = list.stream().filter(e -> e.getName().equals("someOtherProp")).findFirst().get();
        assertNull(someOtherProp.getParent());
    }
}