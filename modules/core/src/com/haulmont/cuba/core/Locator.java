/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 17:58:40
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.sys.LocatorImpl;
import com.haulmont.cuba.core.app.ResourceRepository;
import com.haulmont.cuba.core.app.ResourceRepositoryMBean;

import javax.naming.Context;

/**
 * Locator helps to find EJBs, MBeans and some widely used services.<br>
 * Also serves as Transaction factory.<p>
 * Must be used from inside middleware only.
 */
public abstract class Locator
{
    private static Locator instance;

    private static Locator getInstance() {
        if (instance == null) {
            instance = new LocatorImpl();
        }
        return instance;
    }

    /** Returns current JNDI context */
    public static Context getJndiContext() {
        return getInstance().__getJndiContextImpl();
    }

    /** Lookups local EJB by name (without /local suffix)*/
    public static <T> T lookupLocal(String name) {
        return (T) getInstance().__lookupLocal(name);
    }

    /** Lookups remote EJB by name (without /remote suffix) */
    public static <T> T lookupRemote(String name) {
        return (T) getInstance().__lookupRemote(name);
    }

    /** Lookups MBean by object name */
    public static <T> T lookupMBean(Class<T> mbeanClass, String name) {
        return (T) getInstance().__lookupMBean(mbeanClass, name);
    }

    /** Creates a new Transaction */
    public static Transaction createTransaction() {
        return getInstance().__createTransaction();
    }

    /** Returns reference to ResourceRepository */
    public static ResourceRepository getResourceRepository() {
        ResourceRepositoryMBean mbean = getInstance().__lookupMBean(ResourceRepositoryMBean.class, ResourceRepositoryMBean.OBJECT_NAME);
        return mbean.getImplementation();
    }

    protected abstract Context __getJndiContextImpl();

    protected abstract Object __lookupLocal(String name);

    protected abstract Object __lookupRemote(String name);

    protected abstract <T> T __lookupMBean(Class<T> mbeanClass, String name);

    protected abstract Transaction __createTransaction();
}
