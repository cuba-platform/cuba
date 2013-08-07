/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.09.2009 18:21:39
 *
 * $Id$
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
