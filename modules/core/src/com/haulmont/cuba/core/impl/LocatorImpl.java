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
import com.haulmont.cuba.core.Transaction;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;

import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.mx.util.MBeanProxyExt;

public class LocatorImpl extends Locator
{
    private Context jndiContext;

    private MBeanServer localServer;

    public LocatorImpl() {
        localServer = MBeanServerLocator.locateJBoss();
    }

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

    protected Object __lookupLocal(String name) {
        Context ctx = __getJndiContextImpl();
        try {
            return ctx.lookup(name + "/local");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    protected Object __lookupRemote(String name) {
        Context ctx = __getJndiContextImpl();
        try {
            return ctx.lookup(name + "/remote");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T __lookupMBean(Class<T> mbeanClass, String name) {
        try {
            return (T) MBeanProxyExt.create(mbeanClass, name, localServer);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException("Unable to locate MBean " + name, e);
        }
    }

    protected Transaction __createTransaction() {
        Context ctx = __getJndiContextImpl();
        TransactionManager tm;
        try {
            tm = (TransactionManager) ctx.lookup("java:/TransactionManager");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return new JtaTransaction(tm);
    }

}
