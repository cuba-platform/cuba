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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppPropertiesDatasourceTest extends CubaClientTestCase {

    @BeforeEach
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

    @Test
    public void test_pl_9104() throws Exception {
        AppPropertiesDatasource datasource = new AppPropertiesDatasource();

        AppPropertyEntity e1 = new AppPropertyEntity();
        e1.setName("cuba.cat1.ui.prop1");

        AppPropertyEntity e2 = new AppPropertyEntity();
        e2.setName("cuba.cat2.ui.prop2");

        AppPropertyEntity e3 = new AppPropertyEntity();
        e3.setName("cuba.cat2.ui.prop3");

        List<AppPropertyEntity> list = datasource.createEntitiesTree(Arrays.asList(e1, e2, e3));
/*
         cuba
           cat1
             ui
               prop1
           cat2
             ui
               prop2
               prop3
*/
        assertEquals(8, list.size());

        assertEquals(2, list.stream().filter(e -> e.getName().equals("ui")).count());
    }
}