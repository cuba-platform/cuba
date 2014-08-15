/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.CubaTestCase;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DeprecatedConnectionUrlPropertyTest extends CubaTestCase {

    private static final String VALUE = "http://localhost:8080/cuba-core";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AppContext.setProperty("cuba.connectionUrl", null);
        AppContext.setProperty("cuba.connectionUrlList", null);
    }

    @Override
    public void tearDown() throws Exception {
        AppContext.setProperty("cuba.connectionUrl", null);
        AppContext.setProperty("cuba.connectionUrlList", null);
        super.tearDown();
    }

    public void test() throws Exception {
        String property;
        property = AppContext.getProperty("cuba.connectionUrl");
        assertNull(property);
        property = AppContext.getProperty("cuba.connectionUrlList");
        assertNull(property);

        AppContext.setProperty("cuba.connectionUrl", VALUE);
        property = AppContext.getProperty("cuba.connectionUrl");
        assertEquals(VALUE, property);
        property = AppContext.getProperty("cuba.connectionUrlList");
        assertEquals(VALUE, property);

        AppContext.setProperty("cuba.connectionUrl", null);
        AppContext.setProperty("cuba.connectionUrlList", VALUE);
        property = AppContext.getProperty("cuba.connectionUrl");
        assertEquals(VALUE, property);
        property = AppContext.getProperty("cuba.connectionUrlList");
        assertEquals(VALUE, property);

        // Old property have priority, because it is likely set in production and must override default value set
        // in inherited app.properties
        AppContext.setProperty("cuba.connectionUrl", "aaa");
        AppContext.setProperty("cuba.connectionUrlList", "bbb");
        property = AppContext.getProperty("cuba.connectionUrl");
        assertEquals("aaa", property);
        property = AppContext.getProperty("cuba.connectionUrlList");
        assertEquals("aaa", property);
    }
}
