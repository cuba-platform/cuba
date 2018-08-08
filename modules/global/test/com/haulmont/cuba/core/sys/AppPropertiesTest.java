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

package com.haulmont.cuba.core.sys;

import org.apache.commons.text.StringTokenizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class AppPropertiesTest {

    private static final Logger log = LoggerFactory.getLogger(AppPropertiesTest.class);

    AppProperties appProperties;

    @Before
    public void setUp() {
        AppComponent cuba = new AppComponent("cuba");
        AppComponent reports = new AppComponent("reports");

        reports.addDependency(cuba);

        cuba.setProperty("prop1", "prop1_cuba_val", false);
        cuba.setProperty("prop2", "prop2_cuba_val", true);
        cuba.setProperty("prop3", "prop3_cuba_val", true);
        
        reports.setProperty("prop2", "prop2_reports_val", true);
        reports.setProperty("prop3", "prop3_reports_val", false);
        reports.setProperty("prop4", "prop4_reports_val", true);
        reports.setProperty("prop5", "prop5_reports_val", false);
        reports.setProperty("cronExpr", "*/10 * * * * *", false);

        AppComponents appComponents = new AppComponents("core");
        appComponents.add(cuba);
        appComponents.add(reports);
        appProperties = new AppProperties(appComponents);

        appProperties.setProperty("prop6", "prop6_app_val");
        appProperties.setProperty("prop2", "+prop2_app_val");
        appProperties.setProperty("prop3", "+prop3_app_val");
    }

    @Test
    public void testGetProperty() {
        assertEquals("prop1_cuba_val", appProperties.getProperty("prop1"));

        assertEquals("prop2_cuba_val prop2_reports_val prop2_app_val", appProperties.getProperty("prop2"));

        assertEquals("prop3_reports_val prop3_app_val", appProperties.getProperty("prop3"));

        assertEquals("prop4_reports_val", appProperties.getProperty("prop4"));

        assertEquals("prop5_reports_val", appProperties.getProperty("prop5"));

        assertEquals("prop6_app_val", appProperties.getProperty("prop6"));

        assertNull(appProperties.getProperty("prop7"));

        assertEquals("*/10 * * * * *", appProperties.getProperty("cronExpr"));
    }

    @Test
    public void testInterpolation() {
        appProperties.setProperty("prop10", "abc-${prop1}-${prop2}");

        assertEquals("abc-prop1_cuba_val-prop2_cuba_val prop2_reports_val prop2_app_val", appProperties.getProperty("prop10"));
    }

    @Test
    public void testGetPropertyNames() {
        assertArrayEquals(
                new String[] {"cronExpr", "prop1", "prop2", "prop3", "prop4", "prop5", "prop6"},
                appProperties.getPropertyNames());
    }

    @Test
    public void testEmptyStringSubstitution() {
        AppProperties appProperties = new AppProperties(new AppComponents("test"));
        appProperties.setProperty("refapp.myConfig", "1.xml ${ext.myConfig} 2.xml");
        appProperties.setProperty("ext.myConfig", "");

        String propValue = appProperties.getProperty("refapp.myConfig");
        log.debug("Property value: '" + propValue + "'");

        StringTokenizer tokenizer = new StringTokenizer(propValue);
        String[] locations = tokenizer.getTokenArray();

        Assert.assertArrayEquals(new String[] {"1.xml", "2.xml"}, locations);
    }

    @Test
    public void testSystemPropertyOverride() {
        System.setProperty("prop1", "system_value");
        try {
            assertEquals("system_value", appProperties.getProperty("prop1"));
        } finally {
            System.setProperty("prop1", "");
        }
    }
}