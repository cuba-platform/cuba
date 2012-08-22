/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.11.2008 18:35:16
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.annotation.Nullable;
import java.sql.Connection;

/**
 * Interface used to interact with the persistence context.
 */
public interface EntityManager {

    /**
     * Make an instance managed and persistent.
     *
     * @param entity
     * @throws javax.persistence.EntityExistsException
     *                                  if the entity already exists.
     *                                  (The EntityExistsException may be thrown when the persist
     *                                  operation is invoked, or the EntityExistsException or
     *                                  another PersistenceException may be thrown at flush or
     *                                  commit time.)
     * @throws IllegalArgumentException if not an entity
     */
    void persist(Entity entity);

    /**
     * Merge the state of the given entity into the
     * current persistence context.
     *
     * @param entity
     * @return the instance that the state was merged to
     * @throws IllegalArgumentException if instance is not an
     *                                  entity or is a removed entity
     */
    <T extends Entity> T merge(T entity);

    /**
     * Remove the entity instance.<br>
     * What actually happens depends on {@link #isSoftDeletion} flag
     *
     * @param entity
     * @throws IllegalArgumentException if not an entity
     *                                  or if a detached entity
     */
    void remove(Entity entity);

    /**
     * Find by primary key.
     *
     * @param entityClass
     * @param primaryKey
     * @return the found entity instance or null if the entity does not exist
     * @throws IllegalArgumentException if the first argument does
     *                                  not denote an entity type or the second argument is not a valid type for that entity's primary key
     */
    <T extends Entity> T find(Class<T> entityClass, Object primaryKey);

    /**
     * Get an instance, whose state may be lazily fetched.<br>
     * If the requested instance does not exist in the database,
     * the EntityNotFoundException is thrown when the instance
     * state is first accessed.<br>
     * The application should not expect that the instance state will
     * be available upon detachment, unless it was accessed by the
     * application while the entity manager was open.
     *
     * @param entityClass
     * @param primaryKey
     * @return the found entity instance
     * @throws IllegalArgumentException if the first argument does
     *                                  not denote an entity type or the second argument is not a valid type for that entity's primary key
     * @throws javax.persistence.EntityNotFoundException
     *                                  if the entity state cannot be accessed
     */
    <T extends Entity> T getReference(Class<T> entityClass, Object primaryKey);

    /**
     * Create an instance of Query for executing a Java Persistence query language statement.
     *
     * @return the new query instance
     */
    Query createQuery();

    /**
     * Create an instance of Query for executing a Java Persistence query language statement.
     *
     * @param qlString a Java Persistence query string
     * @return the new query instance
     */
    Query createQuery(String qlString);

    /**
     * Create an type-safe instance of Query for executing a Java Persistence query language statement.
     * Can be used to conveniently perform select queries with runtime result type check.
     *
     * @param qlString    a Java Persistence query string
     * @param resultClass expected result class
     * @return the new query instance
     */
    <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass);

    /**
     * Create an instance of Query for executing
     * a native SQL statement, e.g., for update or delete.
     *
     * @return the new query instance
     */
    Query createNativeQuery();

    /**
     * Create an instance of Query for executing
     * a native SQL statement, e.g., for update or delete.<br>
     * Native Query doesn't support named parameters.
     *
     * @param sqlString a native SQL query string
     * @return the new query instance
     */
    Query createNativeQuery(String sqlString);

    /**
     * Set View for this EntityManager instance.
     * <p>All view fields except declared lazy will be eagerly fetched.</p>
     * @param view view instance. May be null, in this case eager fetching will be performed according to JPA mappings.
     */
    void setView(@Nullable View view);

    /**
     * Adds View for this EntityManager instance.<br/>
     * <p>Eager fetching will be performed for all non-lazy fields specified in all added views.</p>
     * @param view non-null view instance
     */
    void addView(View view);

    /**
     * Ensure all view fields, including lazy, are fetched.
     * @param entity    entity instance
     * @param view      view instance that may be different from views currently set on this EntityManager
     */
    void fetch(Entity entity, View view);

    /**
     * Synchronize the persistence context to the underlying database.
     */
    void flush();

    /**
     * Close an application-managed EntityManager.
     * After the close method has been invoked, all methods
     * on the EntityManager instance and any Query objects obtained
     * from it will throw the IllegalStateException except
     * for getTransaction and isOpen (which will return false).
     * If this method is called when the EntityManager is
     * associated with an active transaction, the persistence
     * context remains managed until the transaction completes.
     *
     * @throws IllegalStateException if the EntityManager is container-managed.
     */
    void close();

    /**
     * Check whether the EntityManager is closed.
     *
     * @return true if the EntityManager is closed
     */
    boolean isClosed();

    /**
     * Determine whether the EntityManager is in SoftDeletion mode.
     *
     * @return true if the EntityManager is in SoftDeletion mode
     */
    boolean isSoftDeletion();

    /**
     * Set SoftDeletion mode for this EntityManager
     *
     * @param softDeletion mode
     */
    void setSoftDeletion(boolean softDeletion);

    /**
     * Return current database connection
     *
     * @return connection
     */
    Connection getConnection();

    /**
     * @return  underlying implementation provided by ORM
     */
    javax.persistence.EntityManager getDelegate();
}
