/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 17:58:40
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.sys.AppContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Locator to find beans and other objects in static context.
 * <p>Consider use of injection instead.</p>
 */
public abstract class Locator {

    private static Context jndiContext;

    /**
     * Lookup bean.
     * @param name  bean name
     * @return      bean instance
     */
    public static <T> T lookup(String name) {
        return (T) AppContext.getBean(name);
    }

    /**
     * Lookup JDBC DataSource.
     * @return      datasource
     */
    public static DataSource getDataSource() {
        return (DataSource) AppContext.getBean("dataSource");
    }

    /**
     * Return current JNDI context.
     * @return      context
     */
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
        return AppContext.getBean(Persistence.NAME, Persistence.class).createTransaction();
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
        return AppContext.getBean(Persistence.NAME, Persistence.class).getTransaction();
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
        return AppContext.getBean(Persistence.NAME, Persistence.class).isInTransaction();
    }
}
