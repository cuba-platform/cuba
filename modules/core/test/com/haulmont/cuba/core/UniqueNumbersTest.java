/*
 * Author: Konstantin Krivopustov
 * Created: 16.05.2009 23:17:34
 * 
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.app.UniqueNumbersMBean;
import com.haulmont.cuba.core.app.UniqueNumbersAPI;

public class UniqueNumbersTest extends CubaTestCase
{
    public void test() {
        UniqueNumbersMBean mBean = Locator.lookupMBean(UniqueNumbersMBean.class, UniqueNumbersMBean.OBJECT_NAME);
        UniqueNumbersAPI un = mBean.getAPI();
        long n = un.getNextNumber("test1");
        assertTrue(n >= 0);
    }
}
