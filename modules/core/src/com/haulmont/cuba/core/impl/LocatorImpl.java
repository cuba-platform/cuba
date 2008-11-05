/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.11.2008 19:02:51
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.PersistenceProvider;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class LocatorImpl extends Locator
{
    private Context jndiContext;
    private PersistenceProvider persistenceProvider;

    protected Context __getJndiContextImpl() {
        if (jndiContext == null) {
            try {
                jndiContext = new InitialContext();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return jndiContext;
    }

    protected PersistenceProvider __getPersistenceProvider() {
        if (persistenceProvider == null) {
            persistenceProvider = new ManagedPersistenceProvider(getJndiContext());
        }
        return persistenceProvider;
    }

    protected Object __lookupLocal(String name) {
        Context ctx = getJndiContext();
        try {
            return ctx.lookup(name + "/local");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

}
