/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.testsupport;

import javax.naming.spi.InitialContextFactory;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Hashtable;

public class TestInitialContextFactory implements InitialContextFactory {

    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return TestContext.getInstance();
    }
}
