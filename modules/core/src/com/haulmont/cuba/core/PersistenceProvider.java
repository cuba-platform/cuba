/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.sys.EntityManagerContext;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.Set;
import java.util.UUID;

/**
 * DEPRECATED - use {@link Persistence} via DI or <code>AppBeans.get(Persistence.class)</code>
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
public abstract class PersistenceProvider {

    private static Persistence getPersistence() {
        return AppBeans.get(Persistence.NAME, Persistence.class);
    }

    /**
     * The DB dialect instance.
     *
     * @return dialect
     */
    public static DbDialect getDbDialect() {
        return getPersistence().getDbDialect();
    }

    /**
     * Creates a new transaction.<br>
     * If there is an active transaction, it will be suspended.
     *
     * @return new transaction
     */
    public static Transaction createTransaction(TransactionParams parameters) {
        return getPersistence().createTransaction(parameters);
    }

    /**
     * Creates a new transaction.<br>
     * If there is an active transaction, it will be suspended.
     *
     * @return new transaction
     */
    public static Transaction createTransaction() {
        return getPersistence().createTransaction();
    }

    /**
     * Creates a new JTA transaction if there is no one at the moment.<br>
     * If a JTA transaction exists, does nothing: subsequent invocations
     * of commit() and end() do not affect the transaction.
     *
     * @return new or existing transaction
     */
    public static Transaction getTransaction() {
        return getPersistence().getTransaction();
    }

    /**
     * Current transaction status.
     *
     * @return true if currently in a transaction
     */
    public static boolean isInTransaction() {
        return getPersistence().isInTransaction();
    }

    /**
     * Returns existing or creates a new transaction-bound EntityManager,
     * which will be closed on transaction commit/rollback.
     * <p>Must be invoked inside transaction.</p>
     *
     * @return EntityManager instance
     */
    public static EntityManager getEntityManager() {
        return getPersistence().getEntityManager();
    }

    /**
     * Returns the set of dirty fields (fields changed since last load from DB).
     *
     * @param entity entity instance
     * @return dirty field names
     */
    public static Set<String> getDirtyFields(Entity entity) {
        return getPersistence().getTools().getDirtyFields(entity);
    }

    /**
     * Returns an ID of directly referenced entity without loading it from DB.
     *
     * @param entity   master entity
     * @param property name of reference property
     * @return UUID of the referenced entity
     * @throws IllegalStateException if the entity is not in Managed state
     */
    public static UUID getReferenceId(Object entity, String property) {
        return getPersistence().getTools().getReferenceId(entity, property);
    }

    /**
     * Checks if the property is loaded from DB.
     *
     * @param entity   entity
     * @param property name of the property
     * @return true if loaded
     * @throws IllegalStateException if the entity is not in Managed state
     */
    public static boolean isLoaded(Object entity, String property) {
        return getPersistence().getTools().isLoaded(entity, property);
    }

    /**
     * Global soft deletion indication. Each new {@link com.haulmont.cuba.core.EntityManager}
     * will be created with the same SoftDeletion value.
     *
     * @return true if soft deletion is on
     */
    public static boolean isSoftDeletion() {
        return getPersistence().isSoftDeletion();
    }

    /**
     * Global soft deletion indication. Each new {@link com.haulmont.cuba.core.EntityManager}
     * will be created with the same SoftDeletion value.
     *
     * @param value true if soft deletion is on
     */
    public static void setSoftDeletion(boolean value) {
        getPersistence().setSoftDeletion(value);
    }

    /**
     * @return main JDBC DataSource
     */
    public static DataSource getDataSource() {
        return getPersistence().getDataSource();
    }

    /**
     * Returns context of the current EntityManager.<br/>
     * If not exists, a new instance of context created and returned.
     *
     * @return context
     */
    @Nonnull
    public static EntityManagerContext getEntityManagerContext() {
        return getPersistence().getEntityManagerContext();
    }
}
