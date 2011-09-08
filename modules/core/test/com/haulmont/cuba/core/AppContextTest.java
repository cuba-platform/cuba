/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 23.12.2009 16:30:12
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.sys.AppContext;

public class AppContextTest extends CubaTestCase {

    public void test() {
        Persistence persistence = AppContext.getBean(Persistence.NAME, Persistence.class);
        assertNotNull(persistence);
    }
}
