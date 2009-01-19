/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 10:29:29
 * $Id$
 */
package com.haulmont.cuba.core;

import junit.framework.TestCase;

import java.io.File;

import org.jboss.embedded.Bootstrap;

public abstract class CubaTestCase extends TestCase
{
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("cuba.UnitTestMode", "true");

        File confDir = new File(System.getProperty("user.dir") + "/build/conf");
        System.setProperty("jboss.server.config.url", confDir.toURI().toString());
        
        System.setProperty(SecurityProvider.IMPL_PROP, "com.haulmont.cuba.core.sys.TestSecurityProvider");

        if (!Bootstrap.getInstance().isStarted()) {
            Bootstrap.getInstance().bootstrap();
        }
    }
}
