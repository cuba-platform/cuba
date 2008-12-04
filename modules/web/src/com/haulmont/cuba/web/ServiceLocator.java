/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.12.2008 14:57:17
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ServiceLocator
{
    private static ServiceLocator instance;

    private Context jndiContext;

    private static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }

    public static <T> T lookup(String jndiName) {
        return (T) getInstance().doLookup(jndiName);
    }

    private Context getJndiContext() {
        if (jndiContext == null) {
            try {
                jndiContext = new InitialContext();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return jndiContext;
    }

    private Object doLookup(String jndiName) {
        try {
            return getJndiContext().lookup(jndiName + "/local");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
