/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.sys.EntityManagerContext;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.Set;
import java.util.UUID;

/**
 * Central interface to provide persistence functionality.
 * <p/>
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Persistence {

    String NAME = "cuba_Persistence";

    /**
     * The DB dialect instance.
     *
     * @return dialect
     */
    DbDialect getDbDialect();

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
     * @return new transaction
     */
    Transaction createTransaction();

    /**
     * Creates a new transaction if there is no one at the moment.<br>
     * If a transaction exists, does nothing: subsequent invocations
     * of commit() and end() do not affect the transaction.
     *
     * @return new or existing transaction
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
     * Returns the set of dirty fields (fields changed since last load from DB).
     *
     * @param entity entity instance
     * @return dirty field names
     */
    Set<String> getDirtyFields(Entity entity);

    /**
     * Returns an ID of directly referenced entity without loading it from DB.
     *
     * @param entity   master entity
     * @param property name of reference property
     * @return UUID of the referenced entity
     * @throws IllegalStateException if the entity is not in Managed state
     */
    UUID getReferenceId(Object entity, String property);

    /**
     * Checks if the property is loaded from DB.
     *
     * @param entity   entity
     * @param property name of the property
     * @return true if loaded
     * @throws IllegalStateException if the entity is not in Managed state
     */
    boolean isLoaded(Object entity, String property);

    /**
     * Global soft deletion indication. Each new {@link com.haulmont.cuba.core.EntityManager}
     * will be created with the same SoftDeletion value.
     *
     * @return true if soft deletion is on
     */
    boolean isSoftDeletion();

    /**
     * Global soft deletion indication. Each new {@link com.haulmont.cuba.core.EntityManager}
     * will be created with the same SoftDeletion value.
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
    @Nonnull
    EntityManagerContext getEntityManagerContext();
}
