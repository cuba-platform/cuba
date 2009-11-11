/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2009 15:49:45
 *
 * $Id$
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
