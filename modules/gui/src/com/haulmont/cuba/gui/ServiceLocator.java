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
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Locator of middleware services for use on presentation layer
 */
public abstract class ServiceLocator
{
    private static Class implClass;

    private static ServiceLocator instance;

    protected transient Context jndiContext;
    protected transient DataService dataService;

    public static void setImplClass(Class implClass) {
        ServiceLocator.implClass = implClass;
    }

    private static ServiceLocator getInstance() {
        if (instance == null) {
            try {
                instance = (ServiceLocator) implClass.newInstance();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    /**
     * JNDI context
     */
    public static Context getJndiContext() {
        return getInstance().__getJndiContext();
    }

    /**
     * Locate service reference by its JNDI name
     */
    public static <T> T lookup(String name) {
        return (T) getInstance().__lookup(name);
    }

    /**
     * Reference to DataService
     */
    public static DataService getDataService() {
        return getInstance().__getDataService();
    }

    private DataService __getDataService() {
        if (dataService == null) {
            dataService = (DataService) __lookup(DataService.NAME);
        }
        return dataService;
    }

    protected abstract Context __getJndiContext();

    protected abstract Object __lookup(String name);

}
