/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author krivopustov
 * @version $Id$
 */
public class JndiContextHolder {

    private static Context jndiContext;

    public static Context getContext() {
        if (jndiContext == null) {
            try {
                jndiContext = new InitialContext();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return jndiContext;
    }
}
