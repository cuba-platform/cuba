/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
