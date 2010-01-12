/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 17:58:40
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.app.ResourceRepositoryAPI;
import com.haulmont.cuba.core.sys.AppContext;

import javax.naming.Context;
import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * Locator helps to find EJBs, MBeans and some widely used services.<br>
 * Also serves as Transaction factory.<p>
 * Must be used from inside middleware only.
 */
public abstract class Locator
{
    public static Locator getInstance() {
        return AppContext.getApplicationContext().getBean("cuba_Locator", Locator.class);
    }

    /** Lookups bean */
    public static <T> T lookup(String name) {
        return (T) getInstance().__lookup(name);
    }

    /** Lookups JDBC DataSource */
    public static DataSource getDataSource() {
        return getInstance().__getDataSource();
    }

    /** Returns current JNDI context */
    public static Context getJndiContext() {
        return getInstance().__getJndiContextImpl();
    }

    /**
     * Lookups local EJB by name (without /local suffix)
     * <p>DEPRECATED - use {@link #lookup(String)} instead
     */
    @Deprecated
    public static <T> T lookupLocal(String name) {
        return (T) getInstance().__lookupLocal(name);
    }

    /**
     * Lookups MBean by interface and object name
     * <p>DEPRECATED - use {@link #lookup(String)} instead
     *
     * @param mbeanClass management interface class
     * @param objecName JMX object name
    */
    @Deprecated
    public static <T> T lookupMBean(Class<T> mbeanClass, String objecName) {
        return (T) getInstance().__lookupMBean(mbeanClass, objecName);
    }

    /**
     * Lookups MBean by interface. Object name should be declared in OBJECT_NAME constant of the interface.
     * <p>DEPRECATED - use {@link #lookup(String)} instead
     *
     * @param mbeanClass management interface class
    */
    @Deprecated
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
     * If there is an active transaction, it will be suspended.
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

    /** True if a JTA transaction is now active */
    public static boolean isInTransaction() {
        return getInstance().__isInTransaction();
    }

    /** Returns reference to ResourceRepositoryAPI */
    public static ResourceRepositoryAPI getResourceRepository() {
        return (ResourceRepositoryAPI) getInstance().__lookup(ResourceRepositoryAPI.NAME);
    }

    protected abstract Object __lookup(String name);

    protected abstract DataSource __getDataSource();

    protected abstract Context __getJndiContextImpl();

    protected abstract Object __lookupLocal(String name);

    protected abstract <T> T __lookupMBean(Class<T> mbeanClass, String objectName);

    protected abstract Transaction __createTransaction();

    protected abstract Transaction __getTransaction();

    protected abstract boolean __isInTransaction();
}
