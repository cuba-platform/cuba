/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 10:23:52
 * $Id$
 */
package com.haulmont.cuba.core;

import javax.transaction.TransactionManager;
import javax.naming.Context;
import javax.naming.NamingException;

public class LocatorTest extends CubaTestCase
{
    public void testJndi() {
        Context ctx = Locator.getJndiContext();
        try {
            TransactionManager tm = (TransactionManager) ctx.lookup("java:/TransactionManager");
            assertNotNull(tm);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
