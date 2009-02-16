/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.02.2009 12:09:16
 * $Id$
 */
package com.haulmont.cuba;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.common.ValueListener;

public class CubaEnchancerTest extends CubaTestCase {
    public void testListeners() {
        final Server server1 = new Server();
        final Server server2 = new Server();

        ((Instance) server1).addListener(new ValueListener() {
            public void propertyChanged(Object item, String property, Object prevValue, Object value) {
                if ("name".equals(property)) {
                    server2.setName((String) value);
                }
            }
        });

        server1.setName("server1");

        assertEquals(server1.getName(), server2.getName());
    }
}
