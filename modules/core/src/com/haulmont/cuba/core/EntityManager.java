/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.annotation.Nullable;
import java.sql.Connection;

/**
 * Interface used to interact with the persistence context.
 *
 * <p>Mostly mimics the {@code javax.persistence.EntityManager} interface and adds methods for working with views and
 * soft deletion.</p>
 *
 * @author krivopustov
 * @version $Id$
 */
public interface EntityManager {

    /**
     * Make an instance managed and persistent.
     *
     * @param entity entity instance
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
     * @param entity    entity instance
     * @return the instance that the state was merged to
     * @throws IllegalArgumentException if instance is not an
     *                                  entity or is a removed entity
     */
    <T extends Entity> T merge(T entity);

    /**
     * Remove the entity instance.
     * What actually happens depends on {@link #isSoftDeletion} flag.
     *
     * @param entity    entity instance
     * @throws IllegalArgumentException if not an entity
     */
    void remove(Entity entity);

    /**
     * Find by primary key.
     *
     * @param entityClass   entity class
     * @param primaryKey    entity id
     * @return the found entity instance or null if the entity does not exist
     * @throws IllegalArgumentException if the first argument does
     *                                  not denote an entity type or the second argument is not a valid type for that entity's primary key
     */
    @Nullable
    <T extends Entity> T find(Class<T> entityClass, Object primaryKey);

    /**
     * Find by primary key.
     * <p/> All non-lazy view properties contained in a combined view are eagerly fetched.
     * A view specified by {@link #setView(View)} method is not taken into account.
     *
     * @param entityClass   entity class
     * @param primaryKey    entity id
     * @param views         array of views
     * @return the found entity instance or null if the entity does not exist
     * @throws IllegalArgumentException if the first argument does
     *                                  not denote an entity type or the second argument is not a valid type for that entity's primary key
     */
    @Nullable
    <T extends Entity> T find(Class<T> entityClass, Object primaryKey, View... views);

    /**
     * Find by primary key.
     * <p/> All non-lazy view properties contained in a combined view are eagerly fetched.
     * A view specified by {@link #setView(View)} method is not taken into account.
     *
     * @param entityClass   entity class
     * @param primaryKey    entity id
     * @param viewNames     array of view names for this entity
     *
     * @return the found entity instance or null if the entity does not exist
     * @throws IllegalArgumentException if the first argument does
     *                                  not denote an entity type or the second argument is not a valid type for that entity's primary key
     */
    @Nullable
    <T extends Entity> T find(Class<T> entityClass, Object primaryKey, String... viewNames);

    /**
     * Get an instance, whose state may be lazily fetched.<br>
     * If the requested instance does not exist in the database,
     * the EntityNotFoundException is thrown when the instance
     * state is first accessed.<br>
     * The application should not expect that the instance state will
     * be available upon detachment, unless it was accessed by the
     * application while the entity manager was open.
     *
     * @param entityClass entity class
     * @param primaryKey entity id
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
     * Create a type-safe instance of Query for executing a Java Persistence query language statement.
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
     * Create an instance of Query for executing
     * a native SQL statement and map its result to an entity.<br>
     * Native Query doesn't support named parameters.
     *
     * @param sqlString a native SQL query string
     * @param resultClass expected result class
     * @return the new query instance
     */
    <T> TypedQuery<T> createNativeQuery(String sqlString, Class<T> resultClass);

    /**
     * Set View for this EntityManager instance and all created {@link Query}s.
     * All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * <p/> WARNING: Use of this method is not recommended, it's better to specify view explicitly in
     * <code>find()</code> methods or in <code>Query</code> instances.
     *
     * @param view view instance. If null, eager fetching is performed according to JPA mappings.
     */
    void setView(@Nullable View view);

    /**
     * Adds View for this EntityManager instance and all created {@link Query}s.
     * All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * <p/> WARNING: Use of this method is not recommended, it's better to specify view explicitly in
     * <code>find()</code> methods or in <code>Query</code> instances.
     *
     * @param view non-null view instance
     */
    void addView(View view);

    /**
     * Ensure all view fields, including lazy, are fetched.
     * @param entity    entity instance
     * @param view      view instance that may be different from views currently set on this EntityManager
     * @throws IllegalArgumentException if the entity is in detached state
     */
    void fetch(Entity entity, View view);

    /**
     * Reload an entity from DB according to a combined view defined by the given array of views.
     * <p/> Ensures all combined view attributes, including lazy, are loaded.
     * <p/> If there is a managed entity with the given id in the current persistence context, the method returns it.
     * Otherwise the method returns a new object instance.
     *
     * @param entityClass   entity class
     * @param id            entity id
     * @param viewNames     array of view names
     * @return              reloaded entity instance, or null if it doesn't exist or has been deleted
     */
    @Nullable
    <T extends Entity> T reload(Class<T> entityClass, Object id, String... viewNames);

    /**
     * Reload an entity from DB according to a combined view defined by the given array of views.
     * <p/> Ensures all combined view attributes, including lazy, are loaded.
     * <p/> If the given entity is in managed state, the method returns the same object instance. If the entity is
     * detached, the method returns a new object instance.
     *
     * @param entity        entity instance to reload
     * @param viewNames     array of view names
     * @return              reloaded entity instance, or null if it has been deleted
     */
    @Nullable
    <T extends Entity> T reload(T entity, String... viewNames);

    /**
     * Synchronize the persistence context to the underlying database.
     */
    void flush();

    /**
     * @return true if the EntityManager is in SoftDeletion mode
     */
    boolean isSoftDeletion();

    /**
     * Set SoftDeletion mode for this EntityManager.
     *
     * @param softDeletion mode
     */
    void setSoftDeletion(boolean softDeletion);

    /**
     * @return database connection associated with the current transaction.
     * <p/> Don't close this connection after use, it will be automatically closed on transaction end.
     */
    Connection getConnection();

    /**
     * @return  underlying implementation provided by ORM
     */
    javax.persistence.EntityManager getDelegate();
}