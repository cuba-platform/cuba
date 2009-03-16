/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.12.2008 14:57:17
 *
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.app.DataService;

import javax.naming.Context;

public abstract class ServiceLocator
{
    public static final String IMPL_PROP = "cuba.ServiceLocator.impl";

    private static final String DEFAULT_IMPL = "com.haulmont.cuba.web.sys.ServiceLocatorImpl";

    private static ServiceLocator instance;

    protected Context jndiContext;
    protected DataService dataService;

    private static ServiceLocator getInstance() {
        if (instance == null) {
            String implClassName = System.getProperty(IMPL_PROP);
            if (implClassName == null)
                implClassName = DEFAULT_IMPL;
            try {
                Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                instance = (ServiceLocator) implClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public static Context getJndiContext() {
        return getInstance().__getJndiContext();
    }

    public static <T> T lookup(String jndiName) {
        return (T) getInstance().__lookup(jndiName);
    }

    public static DataService getDataService() {
        return getInstance().__getBasicService();
    }

    private DataService __getBasicService() {
        if (dataService == null) {
            dataService = (DataService) __lookup(DataService.JNDI_NAME);
        }
        return dataService;
    }

    protected abstract Context __getJndiContext();

    protected abstract Object __lookup(String jndiName);

}
