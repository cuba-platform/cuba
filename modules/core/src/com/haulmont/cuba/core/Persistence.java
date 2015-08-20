/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.sys.EntityManagerContext;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;

import javax.sql.DataSource;

/**
 * Central infrastructure interface to provide persistence functionality.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Persistence {

    String NAME = "cuba_Persistence";


    /**
     * Convenient access to {@link PersistenceTools} bean.
     * @return  PersistenceTools instance
     */
    PersistenceTools getTools();

    /**
     * Returns DbTypeConverter for the current DBMS.
     *
     * @return DbTypeConverter instance
     */
    DbTypeConverter getDbTypeConverter();

    /**
     * Creates a new transaction.<br>
     * If there is an active transaction, it will be suspended.
     *
     * @param params    new transaction parameters
     * @return new transaction
     */
    Transaction createTransaction(TransactionParams params);

    /**
     * Creates a new transaction.<br>
     * If there is an active transaction, it will be suspended.
     *
     * @return object to control the new transaction
     */
    Transaction createTransaction();

    /**
     * Creates a new transaction if there is no one at the moment.
     * <p>If a transaction exists, joins the current transaction. In this case:
     * <ul>
     *     <li>Subsequent invocation of {@link Transaction#commit()} does not affect current transaction.</li>
     *     <li>If {@link Transaction#end()} is called without previous {@link Transaction#commit()}, current
     *     transaction is marked as rollback-only, so any attempt to commit the surrounding {@link Transaction} will
     *     throw an exception.</li>
     * </ul>
     *
     * @return object to control the transaction
     */
    Transaction getTransaction();

    /**
     * Current transaction status.
     *
     * @return true if currently in a transaction
     */
    boolean isInTransaction();

    /**
     * Returns existing or creates a new transaction-bound EntityManager,
     * which will be closed on transaction commit/rollback.
     * <p>Must be invoked inside transaction.</p>
     *
     * @return EntityManager instance
     */
    EntityManager getEntityManager();

    /**
     * Global soft deletion attribute. True by default.
     *
     * @return true if soft deletion is on
     */
    boolean isSoftDeletion();

    /**
     * Set the global soft deletion attribute. The new value affects all {@link EntityManager}s created in
     * new transactions.
     *
     * @param value true if soft deletion is on
     */
    void setSoftDeletion(boolean value);

    /**
     * @return main JDBC DataSource
     */
    DataSource getDataSource();

    /**
     * Returns context of the current EntityManager.<br/>
     * If not exists, a new instance of context created and returned.
     *
     * @return context
     */
    EntityManagerContext getEntityManagerContext();
}
