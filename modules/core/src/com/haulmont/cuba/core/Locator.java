/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 17:58:40
 * $Id$
 */
package com.haulmont.cuba.core;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Locator
{
    private static Context jndiContext;
    private static PersistenceProvider persistenceProvider;

    public static Context getJndiContext() {
        if (jndiContext == null) {
            try {
                jndiContext = new InitialContext();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return jndiContext;
    }

    public static PersistenceProvider getPersistenceProvider() {
        if (persistenceProvider == null) {
            persistenceProvider = new ManagedPersistenceProvider(getJndiContext());
        }
        return persistenceProvider;
    }

    public static CubaEntityManager getEntityManager() {
        return getPersistenceProvider().getEntityManager();
    }

    public static <T> T lookupLocal(String name) {
        Context ctx = getJndiContext();
        try {
            return (T) ctx.lookup(name + "/local");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
