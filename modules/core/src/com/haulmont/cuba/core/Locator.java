/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.AppBeans;

import javax.sql.DataSource;

/**
 * DEPRECATED. Use {@link AppBeans} or dependency injection.
 */
@Deprecated
public abstract class Locator {

    /**
     * Lookup bean.
     * @param name  bean name
     * @return      bean instance
     */
    @Deprecated
    public static <T> T lookup(String name) {
        return (T) AppBeans.get(name);
    }

    /**
     * This is obsolete method.<br/>
     * Use injected {@link Persistence} interface or {@link PersistenceProvider} class instead.
     * <p/>
     * Lookup JDBC DataSource.
     * @return      datasource
     */
    @Deprecated
    public static DataSource getDataSource() {
        return (DataSource) AppBeans.get("dataSource");
    }

    /**
     * This is obsolete method.<br/>
     * Use injected {@link Persistence} interface or {@link PersistenceProvider} class instead.
     * <p/>
     * Creates a new transaction.<br>
     * If there is an active transaction, it will be suspended.
     * @return      new transaction
     */
    @Deprecated
    public static Transaction createTransaction() {
        return AppBeans.get(Persistence.NAME, Persistence.class).createTransaction();
    }

    /**
     * This is obsolete method.<br/>
     * Use injected {@link Persistence} interface or {@link PersistenceProvider} class instead.
     * <p/>
     * Creates a new JTA transaction if there is no one at the moment.<br>
     * If a JTA transaction exists, does nothing: subsequent invocations
     * of commit() and end() do not affect the transaction.
     * @return      new or existing transaction
     */
    @Deprecated
    public static Transaction getTransaction() {
        return AppBeans.get(Persistence.NAME, Persistence.class).getTransaction();
    }

    /**
     * This is obsolete method.<br/>
     * Use injected {@link Persistence} interface or {@link PersistenceProvider} class instead.
     * <p/>
     * Current transaction status
     * @return      true if currently in a transaction
     */
    @Deprecated
    public static boolean isInTransaction() {
        return AppBeans.get(Persistence.NAME, Persistence.class).isInTransaction();
    }
}
