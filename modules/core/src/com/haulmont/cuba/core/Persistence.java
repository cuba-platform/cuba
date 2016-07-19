/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.sys.EntityManagerContext;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;

import javax.sql.DataSource;

/**
 * Central infrastructure interface to provide persistence through {@link com.haulmont.cuba.core.app.RdbmsStore}.
 */
public interface Persistence {

    String NAME = "cuba_Persistence";

    /**
     * Convenient access to {@link PersistenceTools} bean.
     *
     * @return  PersistenceTools instance
     */
    PersistenceTools getTools();

    /**
     * Returns DbTypeConverter for the current DBMS of the main data store.
     *
     * @return DbTypeConverter instance
     */
    DbTypeConverter getDbTypeConverter();

    /**
     * Returns DbTypeConverter for the current DBMS of the specified data store.
     *
     * @param storeName data store name
     * @return DbTypeConverter instance
     */
    DbTypeConverter getDbTypeConverter(String storeName);

    /**
     * Executes the action specified by the given single method object within a new transaction in the main data store.
     *
     * @see #callInTransaction(String, Transaction.Callable)
     * @see #runInTransaction(Transaction.Runnable)
     */
    <T> T callInTransaction(Transaction.Callable<T> callable);

    /**
     * Executes the action specified by the given single method object within a new transaction.
     * <p>Returns a result object created within the transaction.
     * <p>A {@code RuntimeException} thrown in the transactional code enforces a rollback.
     *
     * @param storeName data store name
     * @param callable  transactional code in the form of {@link com.haulmont.cuba.core.Transaction.Callable}
     * @param <T>       result type
     * @return          result object
     * @see #runInTransaction(Transaction.Runnable)
     */
    <T> T callInTransaction(String storeName, Transaction.Callable<T> callable);

    /**
     * Executes the action specified by the given single method object within a new transaction in the main data store.
     *
     * @see #runInTransaction(String, Transaction.Runnable)
     * @see #callInTransaction(Transaction.Callable)
     */
    void runInTransaction(Transaction.Runnable runnable);

    /**
     * Executes the action specified by the given single method object within a new transaction.
     * <p>A {@code RuntimeException} thrown in the transactional code enforces a rollback.
     *
     * @param storeName data store name
     * @param runnable  transactional code in the form of {@link com.haulmont.cuba.core.Transaction.Runnable}
     * @see #callInTransaction(Transaction.Callable)
     */
    void runInTransaction(String storeName, Transaction.Runnable runnable);

    /**
     * Creates a new transaction in the main data store.
     *
     * @see #createTransaction(TransactionParams)
     */
    Transaction createTransaction(TransactionParams params);

    /**
     * Creates a new transaction.<br>
     * If there is an active transaction, it will be suspended.
     *
     * @param storeName data store name
     * @param params    new transaction parameters
     * @return new transaction
     */
    Transaction createTransaction(String storeName, TransactionParams params);

    /**
     * Creates a new transaction in the main data store.<br>
     *
     * @see #createTransaction(String)
     */
    Transaction createTransaction();

    /**
     * Creates a new transaction.<br>
     * If there is an active transaction, it will be suspended.
     *
     * @param storeName data store name
     * @return object to control the new transaction
     */
    Transaction createTransaction(String storeName);

    /**
     * Creates a new transaction in the main data store if there is no one at the moment.
     *
     * @see #getTransaction(String)
     */
    Transaction getTransaction();

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
     * @param storeName data store name
     * @return object to control the transaction
     */
    Transaction getTransaction(String storeName);

    /**
     * Current transaction status.
     *
     * @return true if currently in a transaction
     */
    boolean isInTransaction();

    /**
     * Returns existing or creates a new transaction-bound EntityManager for the main data store,
     * which will be closed on transaction commit/rollback.
     *
     * @see #getTransaction(String)
     */
    EntityManager getEntityManager();

    /**
     * Returns existing or creates a new transaction-bound EntityManager,
     * which will be closed on transaction commit/rollback.
     * <p>Must be invoked inside a transaction.</p>
     *
     * @return EntityManager instance
     */
    EntityManager getEntityManager(String storeName);

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
     * @return JDBC DataSource of the give data store
     */
    DataSource getDataSource(String storeName);

    /**
     * Returns context of the current EntityManager in the main data store.
     *
     * @see #getEntityManagerContext(String)
     * @return context
     */
    EntityManagerContext getEntityManagerContext();

    /**
     * Returns context of the current EntityManager.<br/>
     * If not exists, a new instance of context created and returned.
     *
     * @param storeName data store name
     * @return context
     */
    EntityManagerContext getEntityManagerContext(String storeName);

    /**
     * Destroys the persistence configuration. Further use of this bean instance is impossible.
     */
    void dispose();
}
