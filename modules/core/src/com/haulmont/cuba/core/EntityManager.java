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
 */
public interface EntityManager {

    /**
     * Make an instance managed and persistent.
     *
     * @param entity entity instance
     * @throws IllegalArgumentException if not an entity
     */
    void persist(Entity entity);

    /**
     * Merge the state of the given entity into the current persistence context.
     *
     * @param entity    entity instance
     * @return the instance that the state was merged to
     * @throws IllegalArgumentException if instance is not an entity or is a removed entity
     */
    <T extends Entity> T merge(T entity);

    /**
     * Merge the state of the given entity into the current persistence context.
     * <p>Ensures that all attributes of the specified view are loaded in the returned instance.
     *
     * @param entity    entity instance
     * @param view      view instance
     * @return the instance that the state was merged to
     * @throws IllegalArgumentException if instance is not an entity or is a removed entity
     */
    <T extends Entity> T merge(T entity, @Nullable View view);

    /**
     * Merge the state of the given entity into the current persistence context.
     * <p>Ensures that all attributes of the specified view are loaded in the returned instance.
     *
     * @param entity    entity instance
     * @param viewName  view name
     * @return the instance that the state was merged to
     * @throws IllegalArgumentException if instance is not an entity or is a removed entity
     */
    <T extends Entity> T merge(T entity, @Nullable String viewName);

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
     * @param id            entity id
     * @return the found entity instance or null if the entity does not exist
     * @throws IllegalArgumentException if the first argument does not denote an entity type or the second argument
     * is not a valid type for that entity's primary key
     */
    @Nullable
    <T extends Entity<K>, K> T find(Class<T> entityClass, K id);

    /**
     * Find by primary key.
     *
     * @param entityClass   entity class
     * @param id            entity id
     * @param views         array of views
     * @return the found entity instance or null if the entity does not exist
     * @throws IllegalArgumentException if the first argument does not denote an entity type or the second argument
     * is not a valid type for that entity's primary key
     */
    @Nullable
    <T extends Entity<K>, K> T find(Class<T> entityClass, K id, View... views);

    /**
     * Find by primary key.
     *
     * @param entityClass   entity class
     * @param id            entity id
     * @param viewNames     array of view names for this entity
     *
     * @return the found entity instance or null if the entity does not exist
     * @throws IllegalArgumentException if the first argument does not denote an entity type or the second argument
     * is not a valid type for that entity's primary key
     */
    @Nullable
    <T extends Entity<K>, K> T find(Class<T> entityClass, K id, String... viewNames);

    /**
     * Get an instance, whose state may be lazily fetched.<br>
     * If the requested instance does not exist in the database,
     * the EntityNotFoundException is thrown when the instance
     * state is first accessed.<br>
     * The application should not expect that the instance state will
     * be available upon detachment, unless it was accessed by the
     * application while the entity manager was open.
     *
     * @param entityClass   entity class
     * @param id            entity id
     * @return the found entity instance
     * @throws IllegalArgumentException if the first argument does not denote an entity type or the second argument
     * is not a valid type for that entity's primary key
     * @throws javax.persistence.EntityNotFoundException if the entity state cannot be accessed
     */
    <T extends Entity<K>, K> T getReference(Class<T> entityClass, K id);

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
     * Create an instance of Query for executing a native SQL statement, e.g., for update or delete.
     *
     * @return the new query instance
     */
    Query createNativeQuery();

    /**
     * Create an instance of Query for executing a native SQL statement, e.g., for update or delete.<br>
     * Native Query doesn't support named parameters.
     *
     * @param sqlString a native SQL query string
     * @return the new query instance
     */
    Query createNativeQuery(String sqlString);

    /**
     * Create an instance of Query for executing a native SQL statement and map its result to an entity.<br>
     * Native Query doesn't support named parameters.
     *
     * @param sqlString a native SQL query string
     * @param resultClass expected result class
     * @return the new query instance
     */
    <T> TypedQuery<T> createNativeQuery(String sqlString, Class<T> resultClass);

    /**
     * Reload an entity from DB according to a combined view defined by the given array of views.
     * <p/> Ensures all combined view attributes are loaded.
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
     * Reload an entity from DB according to a combined view defined by the given array of views.
     * <p/> Ensures all combined view attributes are loaded.
     * <p/> If the given entity is in managed state, the method returns the same object instance. If the entity is
     * detached, the method returns a new object instance.
     *
     * @param entity        entity instance to reload
     * @param viewNames     array of view names
     * @return              reloaded entity instance
     * @throws javax.persistence.EntityNotFoundException if the entity has been deleted
     */
    @Nullable
    <T extends Entity> T reloadNN(T entity, String... viewNames);

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
     * <p> Don't close this connection after use, it will be automatically closed on transaction end.
     */
    Connection getConnection();

    /**
     * @return  underlying implementation provided by ORM
     */
    javax.persistence.EntityManager getDelegate();

    /**
     * DEPRECATED since v.6
     */
    @Deprecated
    void fetch(Entity entity, View view);

    /**
     * DEPRECATED since v.6.
     * Use {@link #find(Class, Object, String...)}
     */
    @Deprecated
    @Nullable
    <T extends Entity<K>, K> T reload(Class<T> entityClass, K id, String... viewNames);
}
