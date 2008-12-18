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
import com.haulmont.cuba.core.SecurityProvider;

public class CubaTestCase extends TestCase
{
    protected void setUpDeploymentFiles() {
        TestContainer.addDeploymentFile("cuba-core-global.jar");
        TestContainer.addDeploymentFile("20cuba-core.jar");
    }

    protected void setUp() throws Exception {
        super.setUp();
        if (!TestContainer.isStarted()) {
            setUpDeploymentFiles();
            TestContainer.start();
        }
        System.setProperty(SecurityProvider.IMPL_PROP, "com.haulmont.cuba.core.sys.TestSecurityProvider");
    }
}
