/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DeprecatedPropertyTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private static final String VALUE = "someValue";

    @Before
    public void setUp() throws Exception {
        cleanup();
    }

    @After
    public void tearDown() throws Exception {
        cleanup();
    }

    protected void cleanup() {
        AppContext.setProperty("cuba.connectionUrl", null);
        AppContext.setProperty("cuba.connectionUrlList", null);
        AppContext.setProperty("fts.indexingBatchSize", null);
        AppContext.setProperty("cuba.fts.indexingBatchSize", null);
        AppContext.setProperty("cuba.entityLog.enabled", null);
        AppContext.setProperty("cuba.security.EntityLog.enabled", null);
        AppContext.setProperty("cuba.web.ExternalAuthentication", null);
        AppContext.setProperty("cuba.web.externalAuthentication", null);
        AppContext.setProperty("cuba.reporting.entityTreeModelMaxDeep", null);
        AppContext.setProperty("reporting.entityTreeModelMaxDepth", null);
        AppContext.setProperty("cuba.client.maxUploadSizeMb", null);
        AppContext.setProperty("cuba.maxUploadSizeMb", null);
        AppContext.setProperty("cuba.reporting.useBackgroundReportProcessing", null);
        AppContext.setProperty("reporting.useBackgroundReportProcessing", null);
        AppContext.setProperty("cuba.charts.map.provider", null);
        AppContext.setProperty("charts.map.provider", null);
        AppContext.setProperty("cuba.amazon.s3.accessKey", null);
        AppContext.setProperty("cuba.amazonS3.accessKey", null);
    }

    @Test
    public void testСonnectionUrlProperty() throws Exception {
        propertyTest("cuba.connectionUrl", "cuba.connectionUrlList");
    }

    @Test
    public void testFtsProperties() throws Exception {
        propertyTest("cuba.fts.indexingBatchSize", "fts.indexingBatchSize");
    }

    @Test
    public void testOtherProperties() throws Exception {
        propertyTest("cuba.security.EntityLog.enabled", "cuba.entityLog.enabled");
        propertyTest("cuba.web.ExternalAuthentication", "cuba.web.externalAuthentication");
        propertyTest("cuba.reporting.entityTreeModelMaxDeep", "reporting.entityTreeModelMaxDepth");
        propertyTest("cuba.client.maxUploadSizeMb", "cuba.maxUploadSizeMb");
        propertyTest("cuba.reporting.useBackgroundReportProcessing", "reporting.useBackgroundReportProcessing");
        propertyTest("cuba.charts.map.provider", "charts.map.provider");
        propertyTest("cuba.amazon.s3.accessKey", "cuba.amazonS3.accessKey");
        propertyTest("cuba.gui.tableInsertShortcut", "cuba.gui.tableShortcut.insert");
    }

    private void propertyTest(String oldName, String newName) {
        String property;
        property = AppContext.getProperty(oldName);
        assertNull(property);
        property = AppContext.getProperty(newName);
        assertNull(property);

        AppContext.setProperty(oldName, VALUE);
        property = AppContext.getProperty(oldName);
        assertEquals(VALUE, property);
        property = AppContext.getProperty(newName);
        assertEquals(VALUE, property);

        AppContext.setProperty(oldName, null);
        AppContext.setProperty(newName, VALUE);
        property = AppContext.getProperty(oldName);
        assertEquals(VALUE, property);
        property = AppContext.getProperty(newName);
        assertEquals(VALUE, property);

        // Old property has priority, because it is likely set in production and must override default value set
        // in inherited app.properties
        AppContext.setProperty(oldName, "aaa");
        AppContext.setProperty(newName, "bbb");
        property = AppContext.getProperty(oldName);
        assertEquals("aaa", property);
        property = AppContext.getProperty(newName);
        assertEquals("aaa", property);
    }
}
