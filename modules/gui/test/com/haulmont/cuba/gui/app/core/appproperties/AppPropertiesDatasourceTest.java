/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 *
 */
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