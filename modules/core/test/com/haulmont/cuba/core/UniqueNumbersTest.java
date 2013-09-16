/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

/*
 * Author: Konstantin Krivopustov
 * Created: 16.05.2009 23:17:34
 * 
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.app.UniqueNumbersAPI;

public class UniqueNumbersTest extends CubaTestCase
{
    public void test() {
        UniqueNumbersAPI mBean = Locator.lookup(UniqueNumbersAPI.NAME);
        long n = mBean.getNextNumber("test1");
        assertTrue(n >= 0);
    }
}
