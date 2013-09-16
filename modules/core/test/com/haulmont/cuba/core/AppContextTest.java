/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.AppBeans;

public class AppContextTest extends CubaTestCase {

    public void test() {
        Persistence persistence = AppBeans.get(Persistence.NAME, Persistence.class);
        assertNotNull(persistence);
    }
}
