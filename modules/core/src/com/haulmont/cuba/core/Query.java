/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.annotation.Nullable;
import javax.persistence.LockModeType;
import javax.persistence.TemporalType;
import java.util.List;
import java.util.Date;

/**
 * Interface used to control query execution.
 *
 * <p/> Consider use of {@link TypedQuery} instead of this interface.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Query {

    /**
     * Get the query string.
     * @return  query string
     */
    String getQueryString();

    /**
     * Set the query string.
     * @param queryString   query string
     */
    Query setQueryString(String queryString);

    /**
     * Execute a SELECT query and return the query results as a List.
     * @return a list of the results
     * @throws IllegalStateException if called for a Java Persistence query language UPDATE or DELETE statement
     */
    List getResultList();

    /**
     * Execute a SELECT query that returns a single result.
     * @return the result
     * @throws javax.persistence.NoResultException if there is no result
     * @throws javax.persistence.NonUniqueResultException if more than one result
     * @throws IllegalStateException if called for a Java Persistence query language UPDATE or DELETE statement
     */
    Object getSingleResult();

    /**
     * Execute a SELECT query.<br/>
     * Returns null if there is no result. <br/>
     * Returns first result if more than one result.
     *
     * @return the result
     * @throws IllegalStateException if called for a Java Persistence query language UPDATE or DELETE statement
     */
    @Nullable
    Object getFirstResult();

    /**
     * Execute an update or delete statement.
     * @return the number of entities updated or deleted
     * @throws IllegalStateException if called for a Java Persistence query language SELECT statement
     * @throws javax.persistence.TransactionRequiredException if there is no transaction
     */
    int executeUpdate();

    /**
     * Set the maximum number of results to retrieve.
     * @return the same query instance
     * @throws IllegalArgumentException if argument is negative
     */
    Query setMaxResults(int maxResult);

    /**
     * Set the position of the first result to retrieve.
     * @param startPosition position of the first result, numbered from 0
     * @return the same query instance
     * @throws IllegalArgumentException if argument is negative
     */
    Query setFirstResult(int startPosition);

    /**
     * Bind an argument to a named parameter.<br>
     * Native Query doesn't support named parameters.
     * @param name                      parameter name
     * @param value                     parameter value. Entity instance replaced with its ID.
     * @return the same query instance
     * @throws IllegalArgumentException if parameter name does not correspond to parameter in query string
     * or argument is of incorrect type
     */
    Query setParameter(String name, Object value);

    /**
     * Bind an argument to a named parameter.<br>
     * Native Query doesn't support named parameters.
     * @param name                      parameter name
     * @param value                     parameter value
     * @param implicitConversions       whether to make parameter value conversions, e.g. convert an entity to its ID
     * @return the same query instance
     * @throws IllegalArgumentException if parameter name does not correspond to parameter in query string
     * or argument is of incorrect type
     */
    Query setParameter(String name, Object value, boolean implicitConversions);

    /**
     * Bind an instance of java.util.Date to a named parameter.<br>
     * Native Query doesn't support named parameters.
     * @param name
     * @param value
     * @param temporalType
     * @return the same query instance
     * @throws IllegalArgumentException if parameter name does not correspond to parameter in query string
     */
    Query setParameter(String name, Date value, TemporalType temporalType);

    /**
     * Bind an argument to a positional parameter.
     * @param position                  parameter position, starting with 1
     * @param value                     parameter value. Entity instance replaced with its ID.
     * @return the same query instance
     * @throws IllegalArgumentException if position does not correspond to positional parameter of query
     * or argument is of incorrect type
     */
    Query setParameter(int position, Object value);

    /**
     * Bind an argument to a positional parameter.
     * @param position                  parameter position, starting with 1
     * @param value                     parameter value
     * @param implicitConversions       whether to make parameter value conversions, e.g. convert an entity to its ID
     * @return the same query instance
     * @throws IllegalArgumentException if position does not correspond to positional parameter of query
     * or argument is of incorrect type
     */
    Query setParameter(int position, Object value, boolean implicitConversions);

    /**
     * Bind an instance of java.util.Date to a positional parameter.
     * @param position
     * @param value
     * @param temporalType
     * @return the same query instance
     * @throws IllegalArgumentException if position does not correspond to positional parameter of query
     */
    Query setParameter(int position, Date value, TemporalType temporalType);

    /**
     * Set the lock mode type to be used for the query execution.
     * @param lockMode  lock mode
     * @return          the same query instance
     */
    Query setLockMode(LockModeType lockMode);

    /**
     * Set View for this Query instance.
     * <p/> All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * @param view view instance. If null, eager fetching is performed according to JPA mappings.
     * @return the same query instance
     */
    Query setView(@Nullable View view);

    /**
     * Set View for this Query instance.
     * <p/> All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * @param entityClass   entity class to get a view instance by the name provided
     * @param viewName      view name
     * @return the same query instance
     */
    Query setView(Class<? extends Entity> entityClass, String viewName);

    /**
     * Adds View for this Query instance.
     * <p/> All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * @param view view instance - must not be null
     * @return the same query instance
     */
    Query addView(View view);

    /**
     * Adds View for this Query instance.
     * <p/> All non-lazy view properties contained in a combination of all added views are eagerly fetched.
     *
     * @param entityClass   entity class to get a view instance by the name provided
     * @param viewName      view name - must not be null
     * @return the same query instance
     */
    Query addView(Class<? extends Entity> entityClass, String viewName);

    /**
     * @return  underlying implementation provided by ORM
     */
    javax.persistence.Query getDelegate();
}
