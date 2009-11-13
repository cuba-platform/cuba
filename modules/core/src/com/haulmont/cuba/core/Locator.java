/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 17:58:40
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.app.ResourceRepositoryMBean;
import com.haulmont.cuba.core.app.ResourceRepositoryAPI;

import javax.naming.Context;
import java.lang.reflect.Field;

/**
 * Locator helps to find EJBs, MBeans and some widely used services.<br>
 * Also serves as Transaction factory.<p>
 * Must be used from inside middleware only.
 */
public abstract class Locator
{
    public static final String IMPL_PROP = "cuba.Locator.impl";
    private static final String DEFAULT_IMPL = "com.haulmont.cuba.core.sys.LocatorImpl";

    private volatile static Locator instance;

    public static Locator getInstance() {
        if (instance == null) {
            synchronized (Locator.class) {
                if (instance == null) {
                    String implClassName = System.getProperty(IMPL_PROP);
                    if (implClassName == null)
                        implClassName = DEFAULT_IMPL;
                    try {
                        Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                        instance = (Locator) implClass.newInstance();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
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

    /**
     * Lookups MBean by interface and object name
     *
     * @param mbeanClass management interface class
     * @param name JMX object name 
    */
    public static <T> T lookupMBean(Class<T> mbeanClass, String name) {
        return (T) getInstance().__lookupMBean(mbeanClass, name);
    }

    /**
     * Lookups MBean by interface. Object name should be declared in OBJECT_NAME constant of the interface.
     *
     * @param mbeanClass management interface class
    */
    public static <T> T lookupMBean(Class<T> mbeanClass) {
        String objectName;
        try {
            Field field = mbeanClass.getDeclaredField("OBJECT_NAME");
            objectName = (String) field.get(null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("No OBJECT_NAME field found in " + mbeanClass);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return (T) getInstance().__lookupMBean(mbeanClass, objectName);
    }

    /**
     * Creates a new JTA transaction.<br>
     * @throws IllegalStateException if a JTA transaction exists
    */
    public static Transaction createTransaction() {
        return getInstance().__createTransaction();
    }

    /**
     * Creates a new JTA transaction if there is no one.<br>
     * If a JTA transaction exists, does nothing: subsequent invocations
     * of commit() and end() do not affect the transaction.
    */
    public static Transaction getTransaction() {
        return getInstance().__getTransaction();
    }

    /** Returns reference to ResourceRepositoryAPI */
    public static ResourceRepositoryAPI getResourceRepository() {
        ResourceRepositoryMBean mbean = getInstance().__lookupMBean(ResourceRepositoryMBean.class, ResourceRepositoryMBean.OBJECT_NAME);
        return mbean.getAPI();
    }

    protected abstract Context __getJndiContextImpl();

    protected abstract Object __lookupLocal(String name);

    protected abstract Object __lookupRemote(String name);

    protected abstract <T> T __lookupMBean(Class<T> mbeanClass, String name);

    protected abstract Transaction __createTransaction();

    protected abstract Transaction __getTransaction();
}
