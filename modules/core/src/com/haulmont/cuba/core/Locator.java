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

import javax.naming.Context;

public abstract class Locator
{
    private static Locator instance;

    private static Locator getInstance() {
        if (instance == null) {
            instance = new LocatorImpl();
        }
        return instance;
    }
    
    public static Context getJndiContext() {
        return getInstance().__getJndiContextImpl();
    }

    public static <T> T lookupLocal(String name) {
        return (T) getInstance().__lookupLocal(name);
    }

    public static <T> T lookupRemote(String name) {
        return (T) getInstance().__lookupRemote(name);
    }

    public static <T> T lookupMBean(Class<T> mbeanClass, String name) {
        return (T) getInstance().__lookupMBean(mbeanClass, name);
    }

    public static Transaction createTransaction() {
        return getInstance().__createTransaction();
    }

    protected abstract Context __getJndiContextImpl();

    protected abstract Object __lookupLocal(String name);

    protected abstract Object __lookupRemote(String name);

    protected abstract <T> T __lookupMBean(Class<T> mbeanClass, String name);

    protected abstract Transaction __createTransaction();
}
