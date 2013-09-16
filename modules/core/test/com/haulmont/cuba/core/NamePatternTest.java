/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Server;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;

public class NamePatternTest extends CubaTestCase {

    public void test() {
        Server server = new Server();
        server.setName("orion");
        server.setRunning(false);

        String instanceName = ((Instance) server).getInstanceName();

        assertEquals(InstanceUtils.getInstanceName((Instance) server), instanceName);
    }
}
